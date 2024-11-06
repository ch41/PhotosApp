package com.example.photosapp.ui.image

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.photosapp.common.base.BaseViewModel
import com.example.photosapp.common.utils.NetworkMonitor
import com.example.photosapp.common.utils.Resource
import com.example.photosapp.data.pagination.DefaultPaginator
import com.example.photosapp.domain.model.comment.PostComment
import com.example.photosapp.domain.repository.CommentRepository
import com.example.photosapp.domain.repository.LocalCommentsRepository
import com.example.photosapp.domain.repository.LocalDbRepository
import com.example.photosapp.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(
    networkMonitor: NetworkMonitor,
    private val localDbRepository: LocalDbRepository,
    private val commentRepository: CommentRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val localCommentsRepository: LocalCommentsRepository
) : BaseViewModel(networkMonitor) {

    private var _imageScreenState = MutableStateFlow(ImageScreenState())
    val imageScreenState: StateFlow<ImageScreenState> get() = _imageScreenState

    private val paginator = DefaultPaginator(
        initialKey = _imageScreenState.value.page,
        onLoadUpdated = {
            _imageScreenState.value = _imageScreenState.value.copy(isLoading = it)
        },
        onRequest = { nextPage ->
            commentRepository.getComments(
                userPreferencesRepository.getToken().getOrNull().orEmpty(),
                page = nextPage,
                _imageScreenState.value.imageId
            )
        },
        getNextKey = {
            _imageScreenState.value.page + 1
        },
        onError = {
            _imageScreenState.value = _imageScreenState.value.copy(
                error = it?.localizedMessage,
                isLoading = false
            )
        },
        onSuccess = { items, newKey ->
            _imageScreenState.value = _imageScreenState.value.copy(
                items = _imageScreenState.value.items + items,
                page = newKey,
                endReached = items.size < 20,
                isLoading = false
            )
            localCommentsRepository.insertComments(
                _imageScreenState.value.items,
                _imageScreenState.value.imageId
            )
        },
    )

    fun getImageById() {
        viewModelScope.launch {
            _imageScreenState.value =
                _imageScreenState.value.copy(
                    imageData = localDbRepository.getImageById(
                        _imageScreenState.value.imageId
                    ),
                )

        }
    }
    private fun refreshComments(imageId: Int) {
        viewModelScope.launch {
            val newImages = commentRepository.getComments(
                userPreferencesRepository.getToken().getOrNull().orEmpty(), page = 0,
                imageId = imageId
            ).getOrNull()
            if (newImages != null) {
                _imageScreenState.value =
                    _imageScreenState.value.copy(
                    items = newImages,
                    page = 1,
                    endReached = newImages.size < 20,
                    isLoading = false
                )

            }
        }
    }
    fun removeCommentById(commentId: Int) {
        viewModelScope.launch {
            commentRepository.deleteComment(
                _imageScreenState.value.imageId,
                userPreferencesRepository.getToken().getOrNull().orEmpty(),
                commentId
            ).collect { resource ->
                when (resource) {
                    is Resource.Error -> {
                        _imageScreenState.value =
                            _imageScreenState.value.copy(deleteError = resource.message)
                    }

                    Resource.Loading -> false
                    is Resource.Success -> {
                        _imageScreenState.value = _imageScreenState.value.copy(
                            items = _imageScreenState.value.items.filter { it.id != commentId }
                        )
                        refreshComments(_imageScreenState.value.imageId)
                    }
                }

            }
            localCommentsRepository.deleteCommentId(commentId)
        }

    }

    fun setImageId(imageId: Int) {
        _imageScreenState.value = _imageScreenState.value.copy(imageId = imageId)
    }

    fun loadNextItems() {
        viewModelScope.launch {
            paginator.loadNextItems()
        }
    }

    fun sendComment(commentText: String) {
        viewModelScope.launch {
            commentRepository.postComment(
                PostComment(commentText),
                userPreferencesRepository.getToken().getOrNull().orEmpty(),
                _imageScreenState.value.imageId
            ).collect { resource ->
                when(resource) {
                    is Resource.Error -> {
                        Log.d("errorImageVM", "sendComment: ${resource.message} ")
                    }
                    Resource.Loading -> false
                    is Resource.Success -> {
                        refreshComments(_imageScreenState.value.imageId)
                    }
                }
            }
        }
    }
}