package com.example.photosapp.data.model.auth
import com.google.gson.annotations.SerializedName


data class SignInResponseBody(
    @SerializedName("data")
    val data: Data,
    @SerializedName("status")
    val status: Int?
) {
    data class Data(
        @SerializedName("login")
        val login: String,
        @SerializedName("token")
        val token: String,
        @SerializedName("userId")
        val userId: Int
    )
}