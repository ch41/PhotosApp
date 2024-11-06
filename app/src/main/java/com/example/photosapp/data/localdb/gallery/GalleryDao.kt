package com.example.photosapp.data.localdb.gallery

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GalleryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGallery(galleryItemEntity: List<GalleryItemEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingleGalleryItem(galleryItemEntity: GalleryItemEntity)

    @Query("select * from gallery_entity")
    suspend fun getAllRepositories() : List<GalleryItemEntity>

    @Query("DELETE FROM gallery_entity WHERE id = :id")
    suspend fun deleteGalleryItemById(id: Int)

    @Query("SELECT * FROM gallery_entity WHERE id = :id")
    suspend fun getGalleryItemById(id: Int): GalleryItemEntity?

}