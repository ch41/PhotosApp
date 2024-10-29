package com.example.photosapp.data.model.images


import com.google.gson.annotations.SerializedName

data class GetImagesResponse(
    @SerializedName("data")
    val data: List<ImageResponse>,
    @SerializedName("status")
    val status: Int
)