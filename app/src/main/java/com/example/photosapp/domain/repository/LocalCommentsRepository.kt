package com.example.photosapp.domain.repository

import com.example.photosapp.domain.model.comment.CommentDto
import com.example.photosapp.domain.model.images.ImagesDto

interface LocalCommentsRepository {
    suspend fun insertComments(comments:List<CommentDto>, imageId:Int)
    suspend fun deleteCommentId(commentId:Int)
}