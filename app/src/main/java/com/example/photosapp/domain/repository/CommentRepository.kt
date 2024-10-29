package com.example.photosapp.domain.repository

import com.example.photosapp.common.utils.Resource
import com.example.photosapp.domain.model.comment.CommentDto
import com.example.photosapp.domain.model.comment.DeleteCommentDto
import com.example.photosapp.domain.model.comment.PostComment
import com.example.photosapp.domain.model.comment.PostCommentDto
import com.example.photosapp.domain.model.images.ImagesDto
import kotlinx.coroutines.flow.Flow

interface CommentRepository {
    suspend fun postComment(
        postComment: PostComment,
        accessToken: String,
        imageId: Int
    ): Flow<Resource<PostCommentDto>>

    suspend fun getComments(accessToken: String, page: Int, imageId: Int): Result<List<CommentDto>>

    suspend fun deleteComment(
        imageId: Int,
        accessToken: String,
        commentId: Int
    ): Flow<Resource<DeleteCommentDto>>
}