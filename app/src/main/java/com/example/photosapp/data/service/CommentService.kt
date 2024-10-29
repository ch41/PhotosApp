package com.example.photosapp.data.service

import androidx.annotation.IntRange
import com.example.photosapp.data.model.comment.DeleteCommentResponse
import com.example.photosapp.data.model.comment.GetCommentResponse
import com.example.photosapp.data.model.comment.PostCommentBody
import com.example.photosapp.data.model.comment.PostCommentResponse
import com.example.photosapp.data.model.images.DeleteImageResponse
import com.example.photosapp.data.model.images.GetImagesResponse
import com.example.photosapp.data.model.images.PostImageBody
import com.example.photosapp.data.model.images.PostImageResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CommentService {

    @GET("/api/image/{imageId}/comment")
    suspend fun getComments(
        @Path("imageId") imageId: Int,
        @Query("page") @IntRange(from = 0) page: Int = 0,
        @Header("Access-Token") accessToken: String
    ): GetCommentResponse

    @POST("/api/image/{imageId}/comment")
    suspend fun postComment(
        @Header("Access-Token") accessToken: String,
        @Body postCommentBody: PostCommentBody,
        @Path("imageId") imageId: Int
    ): PostCommentResponse

    @DELETE("/api/image/{imageId}/comment/{commentId}")
    suspend fun deleteComment(
        @Path("imageId") id: Int,
        @Path("commentId") commentId: Int,
        @Header("Access-Token") accessToken: String
    ): DeleteCommentResponse


}