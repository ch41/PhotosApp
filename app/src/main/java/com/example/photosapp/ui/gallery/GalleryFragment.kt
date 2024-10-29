package com.example.photosapp.ui.gallery

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.photosapp.common.base.BaseFragment
import com.example.photosapp.databinding.FragmentPhotosBinding
import com.example.photosapp.ui.gallery.adapter.GalleryAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryFragment : BaseFragment<FragmentPhotosBinding>() {

    private val viewModel: GalleryViewModel by viewModels()

    private lateinit var takePictureLauncher: ActivityResultLauncher<Intent>
    private lateinit var fusedLocationClient: FusedLocationProviderClient

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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        takePictureLauncher =
            this.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val imageBitmap = result.data?.extras?.get("data") as? Bitmap
                    if (imageBitmap != null) {
                        getLocationAndProcessImage(imageBitmap)
                    } else {
                        Toast.makeText(requireContext(), "Something goes wrong", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        binding.galleryRecyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.galleryScreenState.collect { screenState ->
                adapter.submitList(screenState.items)
                screenState.error?.let { errorMessage ->
                    Toast.makeText(context, "End of list!", Toast.LENGTH_SHORT).show()
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
            requestCameraPermission()
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
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            takePictureLauncher.launch(intent)
        } else {
            Log.e("MyFragment", "No camera app available")
        }
    }

    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_REQUEST_CODE
            )
        } else {
            requestLocationPermissionForCamera()
        }
    }

    private fun requestLocationPermissionForCamera() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )
        } else {
            openCameraIntent()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCameraIntent()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Camera permission is required to take pictures",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            LOCATION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocationAndProcessImage(Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888))
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Location permission is required to access your location",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun getLocationAndProcessImage(imageBitmap: Bitmap) {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        val latitude = location.latitude
                        val longitude = location.longitude
                        viewModel.postPhoto(latitude, longitude, imageBitmap)
                    } else {
                        Log.e("MyFragment", "Location is null, requesting location updates.")
//                        requestLocationUpdate(imageBitmap)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("MyFragment", "Failed to get location: ${exception.message}")
                }
        } else {
            requestLocationPermissionForCamera()
        }
    }

    private fun navigateToImage(imageId: Int) {
        val action = GalleryFragmentDirections.actionNavPhotosToImageFragment(imageId)
        findNavController().navigate(action)
    }

    private companion object {
        const val CAMERA_REQUEST_CODE = 100
        const val LOCATION_REQUEST_CODE = 101
    }

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPhotosBinding = FragmentPhotosBinding.inflate(inflater, container, false)
}