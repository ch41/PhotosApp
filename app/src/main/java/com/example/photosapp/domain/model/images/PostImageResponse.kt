package com.example.photosapp.domain.model.images

data class PostImageDto(
    val id: Int,
    val url:String?,
    val date: Int,
    val lat:Double,
    val lng:Double
)

