package com.example.photosapp.domain.repository

import com.example.photosapp.domain.model.images.ImagesDto

interface LocalDbRepository {
    suspend fun insertGallery(gallery:List<ImagesDto>)
    suspend fun getGallery() : List<ImagesDto>
    suspend fun deleteGalleryItemById(id:Int)
    suspend fun getImageById(id:Int) : ImagesDto
}