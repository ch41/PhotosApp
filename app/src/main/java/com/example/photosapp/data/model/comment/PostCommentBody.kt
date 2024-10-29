package com.example.photosapp.data.model.comment

import com.google.gson.annotations.SerializedName

data class PostCommentBody(
    @SerializedName("text")
    val text: String
)
