package com.example.photosapp.ui.map

import androidx.lifecycle.viewModelScope
import com.example.photosapp.common.base.BaseViewModel
import com.example.photosapp.common.utils.NetworkMonitor
import com.example.photosapp.domain.repository.LocalDbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    networkMonitor: NetworkMonitor,
    private val localDbRepository: LocalDbRepository
) : BaseViewModel(networkMonitor) {

    private var _mapScreenState = MutableStateFlow(MapScreenState())
    val mapScreenState: StateFlow<MapScreenState> get() = _mapScreenState

    init {
        getImages()
    }
    private fun getImages(){
        viewModelScope.launch {
            _mapScreenState.value = _mapScreenState.value.copy(images = localDbRepository.getGallery())
        }
    }
}