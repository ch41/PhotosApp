package com.example.photosapp.ui.mainactivity

import android.graphics.Bitmap
import androidx.lifecycle.viewModelScope
import com.example.photosapp.common.base.BaseViewModel
import com.example.photosapp.common.extensions.toBase64
import com.example.photosapp.common.utils.NetworkMonitor
import com.example.photosapp.common.utils.Resource
import com.example.photosapp.domain.model.images.PostImage
import com.example.photosapp.domain.repository.UserPreferencesRepository
import com.example.photosapp.domain.use_case.PostImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    networkMonitor: NetworkMonitor,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val postImageUseCase: PostImageUseCase
) : BaseViewModel(networkMonitor) {

    private val _mainActivityState = MutableStateFlow(MainActivityState())
    val mainActivityState: StateFlow<MainActivityState> get() = _mainActivityState

    fun postPhoto(latitude: Double, longitude: Double, imageBitmap: Bitmap) {
        val postImage =
            PostImage(imageBitmap.toBase64(), getCurrentDateTimeInSeconds(), latitude, longitude)
        viewModelScope.launch {

            postImageUseCase(
                postImage,
                userPreferencesRepository.getToken().getOrNull().orEmpty()
            ).collect { resource ->
                when (resource) {
                    is Resource.Error -> {
                        _mainActivityState.value =
                            MainActivityState(success = false, errorText = resource.message)
                    }

                    Resource.Loading -> {
                        _mainActivityState.value = MainActivityState(loading = true)
                    }

                    is Resource.Success -> {
                        _mainActivityState.value = MainActivityState(success = true)
                    }
                }

            }
        }
    }
}

fun getCurrentDateTimeInSeconds(): Int {
    return (System.currentTimeMillis() / 1000).toInt() // преобразуем миллисекунды в секунды
}
