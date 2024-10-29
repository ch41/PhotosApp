package com.example.photosapp.domain.pagination

interface Paginator<Key, Item> {
    suspend fun loadNextItems()
    fun reset()
}