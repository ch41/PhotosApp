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
    private val userPreferencesRepository: UserPreferencesRepository
) : BaseViewModel(networkMonitor) {

    private val _mainActivityState = MutableStateFlow(MainActivityState())
    val mainActivityState: StateFlow<MainActivityState> get() = _mainActivityState
    init {
        getUserTokenOrEmpty()
    }
    private fun getUserTokenOrEmpty(){
        viewModelScope.launch {
            val token =  userPreferencesRepository.getToken().getOrNull().orEmpty()
            _mainActivityState.value = _mainActivityState.value.copy(emptyToken = token.isEmpty())
        }
    }
}

fun getCurrentDateTimeInSeconds(): Int {
    return (System.currentTimeMillis() / 1000).toInt()
}
