package com.example.photosapp.data.repository

import com.example.photosapp.data.localdb.gallery.GalleryDao
import com.example.photosapp.data.mapper.domainToLocalEntity
import com.example.photosapp.data.mapper.entityToDomain
import com.example.photosapp.data.mapper.toDomain
import com.example.photosapp.data.mapper.toLocalDbEntity
import com.example.photosapp.domain.model.images.ImagesDto
import com.example.photosapp.domain.repository.LocalDbRepository
import javax.inject.Inject

class LocalGalleryRepositoryImpl @Inject constructor(
    private val galleryDao: GalleryDao
) : LocalDbRepository {
    override suspend fun insertGallery(gallery: List<ImagesDto>) {
        galleryDao.insertGallery(gallery.domainToLocalEntity())
    }

    override suspend fun getGallery(): List<ImagesDto> {
        return galleryDao.getAllRepositories().entityToDomain()
    }

    override suspend fun deleteGalleryItemById(id: Int) {
        galleryDao.deleteGalleryItemById(id)
    }

    override suspend fun getImageById(id: Int): ImagesDto {
        return galleryDao.getGalleryItemById(id)?.toDomain() ?: ImagesDto(0,0,0.0,0.0, "Image not found")
    }

    override suspend fun insertSinglePhoto(image: ImagesDto) {
        galleryDao.insertSingleGalleryItem(image.toLocalDbEntity())
    }
}