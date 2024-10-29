package com.example.photosapp.data.localdb.gallery

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "gallery_entity")
data class GalleryItemEntity(
    val date: Int,
    @PrimaryKey
    val id: Int,
    val lat: Double,
    val lng: Double,
    val url: String
)

