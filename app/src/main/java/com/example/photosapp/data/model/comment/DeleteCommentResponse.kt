package com.example.photosapp.data.model.comment

import com.google.gson.annotations.SerializedName

data class DeleteCommentResponse(
    @SerializedName("status")
    val status:Int,
    @SerializedName("data")
    val data: String? = null
)
