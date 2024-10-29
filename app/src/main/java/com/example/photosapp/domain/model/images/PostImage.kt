package com.example.photosapp.domain.model.images


data class PostImage(
    val base64Image: String,
    val date: Int,
    val lat: Double,
    val lng: Double
)
