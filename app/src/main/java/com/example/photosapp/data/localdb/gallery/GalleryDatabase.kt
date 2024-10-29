package com.example.photosapp.data.localdb.gallery

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.photosapp.data.localdb.comments.CommentsDao
import com.example.photosapp.data.localdb.comments.CommentsItemEntity


@Database(entities = [GalleryItemEntity::class, CommentsItemEntity::class], version = 1)
abstract class GalleryDatabase : RoomDatabase(){
    abstract val dao: GalleryDao
    abstract val commentsDao: CommentsDao
}