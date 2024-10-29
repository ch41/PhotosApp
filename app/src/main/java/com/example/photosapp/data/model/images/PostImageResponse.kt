package com.example.photosapp.data.model.images


import com.google.gson.annotations.SerializedName

data class PostImageResponse(
    @SerializedName("data")
    val data: Data,
    @SerializedName("status")
    val status: Int
) {
    data class Data(
        @SerializedName("date")
        val date: Int,
        @SerializedName("id")
        val id: Int,
        @SerializedName("lat")
        val lat: Double,
        @SerializedName("lng")
        val lng: Double,
        @SerializedName("url")
        val url: String
    )
}