package com.example.photosapp.data.repository

import android.util.Log
import com.example.photosapp.common.utils.Resource
import com.example.photosapp.data.mapper.responseToDomain
import com.example.photosapp.data.mapper.toDomain
import com.example.photosapp.data.mapper.toRequestBody
import com.example.photosapp.data.service.CommentService
import com.example.photosapp.domain.model.comment.CommentDto
import com.example.photosapp.domain.model.comment.DeleteCommentDto
import com.example.photosapp.domain.model.comment.PostComment
import com.example.photosapp.domain.model.comment.PostCommentDto
import com.example.photosapp.domain.repository.CommentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class CommentsRepositoryImpl @Inject constructor(
    private val commentService: CommentService
) : CommentRepository {
    override suspend fun postComment(
        postComment: PostComment,
        accessToken: String,
        imageId: Int
    ): Flow<Resource<PostCommentDto>> =
        flow {
            emit(Resource.Loading)
            val response =
                commentService.postComment(accessToken, postComment.toRequestBody(), imageId)
            emit(Resource.Success(response.toDomain()))

        }.catch { error ->
            when (error) {
                is HttpException -> {
                    emit(Resource.Error(message = "Network error: ${error.code()}"))
                }
                is IOException -> emit(Resource.Error("Network error, please check your connection"))
                else -> emit(Resource.Error(error.localizedMessage ?: "An unexpected error"))
            }

        }.flowOn(Dispatchers.IO)

    override suspend fun getComments(
        accessToken: String,
        page: Int,
        imageId: Int
    ): Result<List<CommentDto>> {
        return Result.success(
            commentService.getComments(
                accessToken = accessToken,
                page = page,
                imageId = imageId
            ).data.responseToDomain()
        )
    }

    override suspend fun deleteComment(
        imageId: Int,
        accessToken: String,
        commentId: Int
    ): Flow<Resource<DeleteCommentDto>> =
        flow {
            val response = commentService.deleteComment(imageId, commentId, accessToken)
            if (response.status == 200) {
                emit(Resource.Success(DeleteCommentDto(response.status, response.data)))
            } else emit(Resource.Error("Something goes wrong"))
        }
}