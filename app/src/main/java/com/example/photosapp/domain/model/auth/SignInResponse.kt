package com.example.photosapp.domain.model.auth

data class SignInResponse(
    val data: Data,
    val status: Int?
) {
    data class Data(
        val login: String,
        val token: String,
        val userId: Int
    )
}
