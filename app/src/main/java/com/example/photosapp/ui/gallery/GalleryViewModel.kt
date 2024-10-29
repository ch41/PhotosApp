package com.example.photosapp.ui.gallery

import android.graphics.Bitmap
import androidx.lifecycle.viewModelScope
import com.example.photosapp.common.base.BaseViewModel
import com.example.photosapp.common.extensions.toBase64
import com.example.photosapp.common.utils.NetworkMonitor
import com.example.photosapp.common.utils.Resource
import com.example.photosapp.data.pagination.DefaultPaginator
import com.example.photosapp.domain.model.images.PostImage
import com.example.photosapp.domain.repository.ImagesRepository
import com.example.photosapp.domain.repository.LocalDbRepository
import com.example.photosapp.domain.repository.UserPreferencesRepository
import com.example.photosapp.domain.use_case.PostImageUseCase
import com.example.photosapp.ui.mainactivity.MainActivityState
import com.example.photosapp.ui.mainactivity.getCurrentDateTimeInSeconds
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    networkMonitor: NetworkMonitor,
    private val imagesRepository: ImagesRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val localDbRepository: LocalDbRepository,
    private val postImageUseCase: PostImageUseCase
) : BaseViewModel(networkMonitor) {

    private var _galleryScreenState = MutableStateFlow(GalleryScreenState())
    val galleryScreenState: StateFlow<GalleryScreenState> get() = _galleryScreenState

    private val paginator = DefaultPaginator(
        initialKey = _galleryScreenState.value.page,
        onLoadUpdated = {
            _galleryScreenState.value = _galleryScreenState.value.copy(isLoading = it)
        },
        onRequest = { nextPage ->
            imagesRepository.getImages(
                userPreferencesRepository.getToken().getOrNull().orEmpty(),
                page = nextPage
            )
        },
        getNextKey = {
            _galleryScreenState.value.page + 1
        },
        onError = {
            _galleryScreenState.value = _galleryScreenState.value.copy(
                error = it?.localizedMessage,
                isLoading = false
            )
        },
        onSuccess = { items, newKey ->
            _galleryScreenState.value = _galleryScreenState.value.copy(
                items = _galleryScreenState.value.items + items,
                page = newKey,
                endReached = items.size < 20,
                isLoading = false
            )
            localDbRepository.insertGallery(_galleryScreenState.value.items)
        },
    )

    fun removeImageById(imageId: Int) {
        viewModelScope.launch {
            imagesRepository.deleteImage(
                imageId,
                userPreferencesRepository.getToken().getOrNull().orEmpty()
            ).collect { resource ->
                when (resource) {
                    is Resource.Error -> {
                        _galleryScreenState.value =
                            _galleryScreenState.value.copy(deleteError = resource.message)
                    }

                    Resource.Loading -> false
                    is Resource.Success -> {
                        _galleryScreenState.value = _galleryScreenState.value.copy(
                            items = _galleryScreenState.value.items.filter { it.id != imageId }
                        )
                    }
                }

            }
            localDbRepository.deleteGalleryItemById(imageId)
        }

    }

    fun postPhoto(latitude: Double, longitude: Double, imageBitmap: Bitmap) {
        val postImage =
            PostImage(imageBitmap.toBase64(), getCurrentDateTimeInSeconds(), latitude, longitude)
        viewModelScope.launch {

            postImageUseCase(
                postImage,
                userPreferencesRepository.getToken().getOrNull().orEmpty()
            ).collect { resource ->
                when (resource) {
                    is Resource.Error -> {
                        _galleryScreenState.value =
                            _galleryScreenState.value.copy(error = resource.message)
                    }
                    Resource.Loading -> {}
                    is Resource.Success -> {}
                }

            }
        }
    }

    fun loadNextItems() {
        viewModelScope.launch {
            paginator.loadNextItems()
        }
    }

    init {
        loadNextItems()
    }

}