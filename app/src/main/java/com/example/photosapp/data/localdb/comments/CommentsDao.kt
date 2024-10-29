package com.example.photosapp.data.localdb.comments

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CommentsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(commentItemEntity: List<CommentsItemEntity>)

    @Query("DELETE FROM comments_entity WHERE id = :id")
    suspend fun deleteCommentItemById(id: Int)

    @Query("SELECT * FROM comments_entity WHERE galleryId = :galleryId")
    suspend fun getCommentsByGalleryId(galleryId: Int): List<CommentsItemEntity>
}