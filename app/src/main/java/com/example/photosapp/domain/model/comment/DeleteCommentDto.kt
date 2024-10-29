package com.example.photosapp.domain.model.comment

import com.google.gson.annotations.SerializedName

data class DeleteCommentDto(
    val status:Int,
    val data: String? = null
)
