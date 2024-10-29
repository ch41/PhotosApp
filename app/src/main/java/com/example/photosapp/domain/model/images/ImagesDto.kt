package com.example.photosapp.domain.model.images

data class ImagesDto(
    val date: Int,
    val id: Int,
    val lat: Double,
    val lng: Double,
    val url: String
)