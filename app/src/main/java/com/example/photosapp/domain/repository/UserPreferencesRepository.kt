package com.example.photosapp.domain.repository

interface UserPreferencesRepository {

    suspend fun setToken(
        name: String
    )

    suspend fun getToken(): Result<String>
}