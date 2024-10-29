package com.example.photosapp.data.model.comment


import com.google.gson.annotations.SerializedName

 data class GetCommentResponse(
    @SerializedName("data")
    val data: List<Comment>,
    @SerializedName("status")
    val status: Int
) {
     data class Comment(
         @SerializedName("date")
         val date: Int,
         @SerializedName("id")
         val id: Int,
         @SerializedName("text")
         val text: String
     )
 }

