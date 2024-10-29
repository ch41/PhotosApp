package com.example.photosapp.di

import android.content.Context
import androidx.room.Room
import com.example.photosapp.common.utils.NetworkMonitor
import com.example.photosapp.data.localdb.comments.CommentsDao
import com.example.photosapp.data.localdb.gallery.GalleryDao
import com.example.photosapp.data.localdb.gallery.GalleryDatabase
import com.example.photosapp.data.repository.AuthenticationRepositoryImpl
import com.example.photosapp.data.service.AuthenticationService
import com.example.photosapp.data.service.CommentService
import com.example.photosapp.data.service.ImagesService
import com.example.photosapp.domain.repository.AuthenticationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val BASE_URL = "https://junior.balinasoft.com/"

    @Provides
    fun provideNetworkMonitor(@ApplicationContext context: Context) = NetworkMonitor(context)

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideAuthenticationService(retrofit: Retrofit): AuthenticationService {
        return retrofit.create(AuthenticationService::class.java)
    }

    @Provides
    fun provideImagesService(retrofit: Retrofit): ImagesService {
        return retrofit.create(ImagesService::class.java)
    }

    @Provides
    fun provideCommentService(retrofit: Retrofit): CommentService {
        return retrofit.create(CommentService::class.java)
    }

    @Provides
    fun provideGalleryDao(database: GalleryDatabase): GalleryDao = database.dao

    @Provides
    fun provideCommentsDao(database: GalleryDatabase): CommentsDao = database.commentsDao

    @Provides
    fun provideGalleryDatabase(@ApplicationContext app: Context): GalleryDatabase =
        Room.databaseBuilder(app, GalleryDatabase::class.java, "gallery_database")
            .fallbackToDestructiveMigration()
            .build()

}