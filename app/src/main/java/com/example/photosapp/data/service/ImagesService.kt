package com.example.photosapp.data.service

import androidx.annotation.IntRange
import com.example.photosapp.data.model.images.DeleteImageResponse
import com.example.photosapp.data.model.images.PostImageBody
import com.example.photosapp.data.model.images.PostImageResponse
import com.example.photosapp.data.model.images.GetImagesResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ImagesService {
    @GET("api/image")
    suspend fun getImages(
        @Query("page") @IntRange(from = 0) page: Int = 0,
        @Header("Access-Token") accessToken: String
    ): GetImagesResponse

    @POST("api/image")
    suspend fun postImage(
        @Header("Access-Token") accessToken: String,
        @Body postImageBody: PostImageBody
    ): PostImageResponse

    @DELETE("api/image/{id}")
    suspend fun deleteImage(
        @Path("id") id: Int,
        @Header("Access-Token") accessToken: String
    ): DeleteImageResponse

}