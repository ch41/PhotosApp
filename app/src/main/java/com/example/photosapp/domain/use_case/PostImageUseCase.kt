package com.example.photosapp.domain.use_case

import com.example.photosapp.common.utils.Resource
import com.example.photosapp.domain.model.images.PostImage
import com.example.photosapp.domain.model.images.PostImageDto
import com.example.photosapp.domain.repository.ImagesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PostImageUseCase @Inject constructor(
    private val imageRepository: ImagesRepository,
) {
    operator fun invoke(
        imageData: PostImage,
        accessToken: String
    ): Flow<Resource<PostImageDto>> = flow {

        emit(Resource.Loading)

        imageRepository.postImage(imageData, accessToken).collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    emit(Resource.Success(resource.data))
                }
                is Resource.Error -> {
                    emit(Resource.Error(resource.message))
                }

                is Resource.Loading -> {
                    emit(Resource.Loading)
                }
            }
        }
    }.flowOn(Dispatchers.IO)
}