package com.example.photosapp.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : BaseFragment<FragmentMapBinding>(), OnMapReadyCallback {

    private val viewModel: MapViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.mapScreenState.collect { screenState ->
                for (image in screenState.images) {
                    val position = LatLng(image.lat, image.lng)
                    googleMap.addMarker(MarkerOptions().position(position).title("Image ID: ${image.id}"))
                }
            }
        }
    }

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?)
            : FragmentMapBinding = FragmentMapBinding.inflate(inflater, container, false)

}