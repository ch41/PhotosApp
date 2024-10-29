package com.example.photosapp.data.model.images


import com.google.gson.annotations.SerializedName

data class DeleteImageResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("data")
    val id: String?,

)
