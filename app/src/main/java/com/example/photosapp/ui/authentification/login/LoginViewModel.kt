package com.example.photosapp.ui.authentification.login

import androidx.lifecycle.viewModelScope
import com.example.photosapp.common.base.BaseViewModel
import com.example.photosapp.common.utils.NetworkMonitor
import com.example.photosapp.common.utils.Resource
import com.example.photosapp.domain.model.auth.SignIn
import com.example.photosapp.domain.repository.UserPreferencesRepository
import com.example.photosapp.domain.use_case.SignInUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    networkMonitor: NetworkMonitor,
    private val signInUserUseCase: SignInUserUseCase,
    private val userPreferencesRepository: UserPreferencesRepository
) : BaseViewModel(networkMonitor) {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> get() = _loginState

    fun signIn(signInCredentials: SignIn) {
        viewModelScope.launch {
            signInUserUseCase(signInCredentials).collect { resource ->

                when (resource) {
                    is Resource.Error -> {
                        _loginState.value = LoginState(errorText = resource.message)
                    }

                    Resource.Loading -> {
                        _loginState.value = LoginState(isLoading = true)
                    }

                    is Resource.Success -> {
                        userPreferencesRepository.setToken(resource.data.data.token)
                        _loginState.value =
                            LoginState(success = true, signInResponse = resource.data)
                   }
                }
            }
        }
    }

    fun resetState() {
        _loginState.value = LoginState()
    }
}
