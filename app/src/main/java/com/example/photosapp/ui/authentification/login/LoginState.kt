package com.example.photosapp.ui.authentification.login

import com.example.photosapp.domain.model.auth.SignInResponse

data class LoginState(
    val errorText: String? = null,
    val isLoading: Boolean = false,
    val success:Boolean = false,
    val signInResponse: SignInResponse? = null
)
