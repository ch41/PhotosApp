package com.example.photosapp.ui.authentification.register

import androidx.lifecycle.viewModelScope
import com.example.photosapp.common.base.BaseViewModel
import com.example.photosapp.common.utils.NetworkMonitor
import com.example.photosapp.common.utils.Resource
import com.example.photosapp.domain.model.auth.SignUp
import com.example.photosapp.domain.use_case.SignUpUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    networkMonitor: NetworkMonitor,
    private val signUpUserUseCase: SignUpUserUseCase
) : BaseViewModel(networkMonitor) {

    private val _registerState = MutableStateFlow(RegisterState())
    val registerState: StateFlow<RegisterState> get() = _registerState

    fun signUp(signUpCredentials: SignUp) {
        viewModelScope.launch {
            signUpUserUseCase(signUpCredentials).collect { resource ->

                when (resource) {
                    is Resource.Error -> {
                        _registerState.value = RegisterState(errorText = resource.message)
                    }

                    Resource.Loading -> {
                        _registerState.value = RegisterState(isLoading = true)
                    }

                    is Resource.Success -> {
                        _registerState.value =
                            RegisterState(success = true, signUpResponse = resource.data)
                    }
                }
            }
        }
    }

    fun resetState() {
        _registerState.value = RegisterState()
    }
}