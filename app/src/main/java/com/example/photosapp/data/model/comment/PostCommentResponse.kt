package com.example.photosapp.data.model.comment


import com.google.gson.annotations.SerializedName

data class PostCommentResponse(
    @SerializedName("data")
    val data: Data,
    @SerializedName("status")
    val status: Int
) {
    data class Data(
        @SerializedName("date")
        val date: Int,
        @SerializedName("id")
        val id: Int,
        @SerializedName("text")
        val text: String
    )
}

