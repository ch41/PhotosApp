package com.example.photosapp.data.repository

import com.example.photosapp.data.localdb.comments.CommentsDao
import com.example.photosapp.data.localdb.comments.CommentsItemEntity
import com.example.photosapp.data.mapper.dtoToEntity
import com.example.photosapp.domain.model.comment.CommentDto
import com.example.photosapp.domain.repository.LocalCommentsRepository
import javax.inject.Inject

class LocalCommentsRepositoryImpl @Inject constructor(
    private val commentsDao: CommentsDao
) : LocalCommentsRepository {
    override suspend fun insertComments(comments: List<CommentDto>, imageId: Int) {
        commentsDao.insertComment(comments.dtoToEntity(imageId))
    }

    override suspend fun deleteCommentId(commentId: Int) {
        commentsDao.deleteCommentItemById(commentId)
    }

    suspend fun getCommentsForGallery(galleryId: Int): List<CommentsItemEntity> {
        return commentsDao.getCommentsByGalleryId(galleryId)
    }

}