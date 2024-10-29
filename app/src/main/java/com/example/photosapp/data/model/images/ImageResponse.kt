package com.example.photosapp.data.model.images


import com.google.gson.annotations.SerializedName

data class ImageResponse(
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