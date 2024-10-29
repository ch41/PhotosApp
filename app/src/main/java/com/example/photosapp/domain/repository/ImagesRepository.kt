package com.example.photosapp.domain.repository

import com.example.photosapp.common.utils.Resource
import com.example.photosapp.domain.model.images.ImagesDto
import com.example.photosapp.domain.model.images.PostImage
import com.example.photosapp.domain.model.images.PostImageDto
import kotlinx.coroutines.flow.Flow

interface ImagesRepository {
    suspend fun postImage(
        postImage: PostImage,
        accessToken: String
    ): Flow<Resource<PostImageDto>>

    suspend fun getImages(accessToken: String, page:Int) : Result<List<ImagesDto>>

    suspend fun deleteImage(imageId:Int, accessToken:String) :Flow<Resource<Boolean>>
}