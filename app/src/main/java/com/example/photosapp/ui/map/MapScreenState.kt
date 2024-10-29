package com.example.photosapp.ui.map

import com.example.photosapp.domain.model.images.ImagesDto

data class MapScreenState(
    val images: List<ImagesDto> = emptyList()
)