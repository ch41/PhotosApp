package com.example.photosapp.ui.authentification.register

import com.example.photosapp.domain.model.auth.SignUpResponse

data class RegisterState(
    val errorText: String? = null,
    val isLoading: Boolean = false,
    val success:Boolean = false,
    val signUpResponse: SignUpResponse? = null
)
