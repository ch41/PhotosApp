package com.example.photosapp.ui.map

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.photosapp.R
import com.example.photosapp.common.base.BaseFragment
import com.example.photosapp.databinding.FragmentMapBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.vmadalin.easypermissions.EasyPermissions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : BaseFragment<FragmentMapBinding>(), OnMapReadyCallback,
    EasyPermissions.PermissionCallbacks {

    private val viewModel: MapViewModel by viewModels()

    companion object {
        const val REQUEST_CODE_LOCATION = 1001
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (EasyPermissions.hasPermissions(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            loadMap()
        } else {
            requestLocationPermissions()
        }
    }

    private fun loadMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun requestLocationPermissions() {
        EasyPermissions.requestPermissions(
            this,
            "Для работы с картой необходим доступ к местоположению.",
            REQUEST_CODE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    override fun onMapReady(googleMap: GoogleMap) {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.mapScreenState.collect { screenState ->
                for (image in screenState.images) {
                    Log.d("imagesMap", "onMapReady: $image ")
                    val position = LatLng(image.lat, image.lng)
                    googleMap.addMarker(
                        MarkerOptions().position(position).title("Image ID: ${image.id}")
                    )
                }
            }
        }
    }
    override fun onResume() {
        super.onResume()
        viewModel.getImages()
    }
    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?)
            : FragmentMapBinding = FragmentMapBinding.inflate(inflater, container, false)

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        Toast.makeText(requireContext(), "Разрешения не предоставлены", Toast.LENGTH_SHORT).show()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        loadMap()
    }

}