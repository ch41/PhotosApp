package com.example.photosapp.data.repository

import android.util.Log
import com.example.photosapp.common.utils.Resource
import com.example.photosapp.data.mapper.toDomain
import com.example.photosapp.data.mapper.toRequestBody
import com.example.photosapp.data.service.ImagesService
import com.example.photosapp.domain.model.images.ImagesDto
import com.example.photosapp.domain.model.images.PostImage
import com.example.photosapp.domain.model.images.PostImageDto
import com.example.photosapp.domain.repository.ImagesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ImagesRepositoryImpl @Inject constructor(
    private val imagesService: ImagesService
) : ImagesRepository {

    override suspend fun postImage(
        postImage: PostImage,
        accessToken: String
    ): Flow<Resource<PostImageDto>> =
        flow {
            Log.d("PostImageUseCase", "postPhoto: $postImage ")
            emit(Resource.Loading)
            val response = imagesService.postImage(accessToken, postImage.toRequestBody())
            Log.d("response", "postImage: $response")
            emit(Resource.Success(response.toDomain()))

        }.catch { error ->
            when (error) {
                is HttpException -> {
                    when (error.code()) {
                        400 -> emit(Resource.Error(message = "Bad Image: ${error.message()}"))
                        500 -> emit(Resource.Error(message = "File Upload Error: ${error.message()}"))
                        else -> emit(Resource.Error(message = "Network error: ${error.code()}"))
                    }
                }

                is IOException -> emit(Resource.Error("Network error, please check your connection"))
                else -> emit(Resource.Error(error.localizedMessage ?: "An unexpected error"))
            }

        }.flowOn(Dispatchers.IO)

    override suspend fun getImages(accessToken: String, page: Int): Result<List<ImagesDto>> {
        return Result.success(
            imagesService.getImages(
                accessToken = accessToken,
                page = page
            ).data.toDomain()
        )
    }

    override suspend fun deleteImage(imageId: Int, accessToken: String): Flow<Resource<Boolean>> =
        flow {
            val response = imagesService.deleteImage(imageId, accessToken)
            if(response.status == 200 ) {
                emit(Resource.Success(true))
            }else emit(Resource.Error("Something goes wrong"))
        }

}