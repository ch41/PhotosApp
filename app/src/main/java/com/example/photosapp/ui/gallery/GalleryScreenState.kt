package com.example.photosapp.ui.gallery

import com.example.photosapp.domain.model.images.ImagesDto

data class GalleryScreenState(
    val isLoading: Boolean = false,
    val items: List<ImagesDto> = emptyList(),
    val error: String? = null,
    val endReached: Boolean = false,
    val page: Int = 0,
    val deleteError:String? = null,
    val imageRemoved:Boolean = false
)
