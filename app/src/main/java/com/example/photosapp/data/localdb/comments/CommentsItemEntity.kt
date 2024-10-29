package com.example.photosapp.data.localdb.comments

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.photosapp.data.localdb.gallery.GalleryItemEntity

@Entity(
    tableName = "comments_entity",
    foreignKeys = [
        ForeignKey(
            entity = GalleryItemEntity::class,
            parentColumns = ["id"],
            childColumns = ["galleryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class CommentsItemEntity(
    val date: Int,
    @PrimaryKey
    val id: Int,
    val text: String,
    val galleryId: Int
)
