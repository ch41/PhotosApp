package com.example.photosapp.domain.model.auth

data class SignUp(
    val login: String,
    val password: String,
    val repeatPassword: String
)
