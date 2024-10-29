package com.example.photosapp.domain.model.comment


data class PostCommentDto(
    val data: Data,
    val status: Int
) {
    data class Data(
        val date: Int,
        val id: Int,
        val text: String
    )
}

