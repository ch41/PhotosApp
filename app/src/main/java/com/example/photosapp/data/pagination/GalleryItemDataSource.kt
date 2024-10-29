package com.example.photosapp.data.pagination

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.photosapp.data.mapper.toDomain
import com.example.photosapp.data.service.ImagesService
import com.example.photosapp.domain.model.images.ImagesDto
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GalleryItemDataSource @Inject constructor(
    private val imagesService: ImagesService,
    private val accessToken: String
) :
    PagingSource<Int, ImagesDto>() {
    companion object {
        private const val DEFAULT_PAGE_SIZE = 3
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImagesDto> {

        val position: Int = params.key ?: 0

        return try {

            val response = imagesService.getImages(
                page = position,
                accessToken = accessToken,
            ).data.toDomain()
            Log.d("getGalleryImages", "load: $response")

            val nextKey = if (response.isEmpty()) {
                null
            } else {
                position + (params.loadSize / DEFAULT_PAGE_SIZE)
            }

            LoadResult.Page(
                data = response,
                prevKey = if (position == 1) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ImagesDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }

    }
}