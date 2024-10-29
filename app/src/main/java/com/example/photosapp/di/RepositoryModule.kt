package com.example.photosapp.di

import com.example.photosapp.data.repository.AuthenticationRepositoryImpl
import com.example.photosapp.data.repository.CommentsRepositoryImpl
import com.example.photosapp.data.repository.ImagesRepositoryImpl
import com.example.photosapp.data.repository.LocalCommentsRepositoryImpl
import com.example.photosapp.data.repository.LocalGalleryRepositoryImpl
import com.example.photosapp.domain.repository.AuthenticationRepository
import com.example.photosapp.domain.repository.CommentRepository
import com.example.photosapp.domain.repository.ImagesRepository
import com.example.photosapp.domain.repository.LocalCommentsRepository
import com.example.photosapp.domain.repository.LocalDbRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindImagesRepository(
        imagesRepositoryImpl: ImagesRepositoryImpl
    ): ImagesRepository

    @Binds
    @Singleton
    abstract fun bindLocalDbRepository(
        imagesRepositoryImpl: LocalGalleryRepositoryImpl
    ): LocalDbRepository

    @Binds
    @Singleton
    abstract fun bindAuthenticationRepository(
        authenticationRepositoryImpl: AuthenticationRepositoryImpl
        ) : AuthenticationRepository

    @Binds
    @Singleton
    abstract fun bindCommentsRepository(
        commentsRepositoryImpl: CommentsRepositoryImpl
    ) : CommentRepository

    @Binds
    @Singleton
    abstract fun bindLocalCommentsRepository(
        commentsRepositoryImpl: LocalCommentsRepositoryImpl
    ) : LocalCommentsRepository


}