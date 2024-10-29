package com.example.photosapp.data.model.images

import com.google.gson.annotations.SerializedName

data class PostImageBody(
    @SerializedName("base64Image")
    val base64Image: String,
    @SerializedName("date")
    val date: Int,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double
)