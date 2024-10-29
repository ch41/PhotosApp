package com.example.photosapp.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.photosapp.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(
    private val userDataStorePreferences: DataStore<Preferences>
) : UserPreferencesRepository {

    override suspend fun setToken(
        name: String
    ) {
        Result.runCatching {
            userDataStorePreferences.edit { preferences ->
                preferences[KEY_NAME] = name
            }
        }
    }

    override suspend fun getToken(): Result<String> {
        return Result.runCatching {
            val flow = userDataStorePreferences.data
                .catch { exception ->
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }
                .map { preferences ->
                    preferences[KEY_NAME]
                }
            val value = flow.firstOrNull() ?: ""
            value
        }
    }

    private companion object {
        val KEY_NAME = stringPreferencesKey(
            name = "token"
        )
    }
}