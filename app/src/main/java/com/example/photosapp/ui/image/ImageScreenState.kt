package com.example.photosapp.ui.image

import com.example.photosapp.domain.model.comment.CommentDto
import com.example.photosapp.domain.model.images.ImagesDto

data class ImageScreenState(
    val imageData: ImagesDto? = null,
    val isLoading: Boolean = false,
    val items: List<CommentDto> = emptyList(),
    val error: String? = null,
    val endReached: Boolean = false,
    val page: Int = 0,
    val deleteError:String? = null,
    val imageRemoved:Boolean = false,
    val imageId:Int = 0
)

