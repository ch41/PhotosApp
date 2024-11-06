package com.example.photosapp.ui.gallery

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.photosapp.common.base.BaseFragment
import com.example.photosapp.common.extensions.resizeAndRotate
import com.example.photosapp.databinding.FragmentPhotosBinding
import com.example.photosapp.ui.gallery.adapter.GalleryAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.vmadalin.easypermissions.EasyPermissions
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class GalleryFragment : BaseFragment<FragmentPhotosBinding>(), EasyPermissions.PermissionCallbacks {

    private val viewModel: GalleryViewModel by viewModels()

    private lateinit var takePictureLauncher: ActivityResultLauncher<Intent>
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var photoUri: Uri
    private val adapter by lazy {
        GalleryAdapter(
            removeOnItemLongClick = { image ->
                showDeleteConfirmationDialog(image)
            },
            navigateToImage = { imageId ->
                navigateToImage(imageId)
            }
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleBackPress()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        takePictureLauncher =
            this.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val imageBitmapHQ = BitmapFactory.decodeStream(
                        requireContext().contentResolver.openInputStream(photoUri)
                    ).resizeAndRotate(photoUri, requireContext(), 1280)
                    getLocationAndProcessImage(imageBitmapHQ)
                }
            }
        binding.galleryRecyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.galleryScreenState.collect { screenState ->
                adapter.submitList(screenState.items)
//                {
//                    if (screenState.scrollToTop) {
//                        binding.galleryRecyclerView.scrollToPosition(0)
//                        viewModel.resetScrollFlag()
//                    }
//                }
                screenState.deleteError?.let { errorMessage ->
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    viewModel.resetDeleteError()
                }
            }
        }
        binding.galleryRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                if (layoutManager.findLastCompletelyVisibleItemPosition() == adapter.itemCount - 1) {
                    viewModel.loadNextItems()
                }
            }
        })
        binding.fab.setOnClickListener {
            requestPermissions()
        }
    }

    private fun showDeleteConfirmationDialog(imageId: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("Подтверждение удаления")
            .setMessage("Вы уверены, что хотите удалить эту фотографию??")
            .setPositiveButton("Удалить") { _, _ ->
                viewModel.removeImageById(imageId)
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun openCameraIntent() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile =
            File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "photo.jpg")
        photoUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            photoFile
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            takePictureLauncher.launch(intent)
        } else {
            Log.e("MyFragment", "No camera app available")
        }
    }

    private fun getLocationAndProcessImage(imageBitmap: Bitmap) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        val latitude = location.latitude
                        val longitude = location.longitude
                        viewModel.postPhoto(latitude, longitude, imageBitmap)
                    } else {
                        Log.e("MyFragment", "Location is null, requesting location updates.")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("MyFragment", "Failed to get location: ${exception.message}")
                }
        }
    }

    private fun navigateToImage(imageId: Int) {
        val action = GalleryFragmentDirections.actionNavPhotosToImageFragment(imageId)
        findNavController().navigate(action)
    }

    private fun requestPermissions() {
        val requiredPermissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (EasyPermissions.hasPermissions(requireContext(), *requiredPermissions.toTypedArray())) {
            openCameraIntent()
        } else {
            EasyPermissions.requestPermissions(
                this,
                "Для работы этой функции необходим доступ к камере и местоположению",
                REQUEST_CODE_CAMERA_AND_LOCATION,
                *requiredPermissions.toTypedArray()
            )
        }
        /*if (EasyPermissions.hasPermissions(
                requireContext(),
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            openCameraIntent()
        } else {
            EasyPermissions.requestPermissions(
                this,
                "Для работы этой функции необходим доступ к камере и местоположению",
                REQUEST_CODE_CAMERA_AND_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }*/
    }


    private companion object {
        const val REQUEST_CODE_CAMERA_AND_LOCATION = 102
    }

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPhotosBinding = FragmentPhotosBinding.inflate(inflater, container, false)

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {

    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.hasPermissions(
                requireContext(),
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            openCameraIntent()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

}