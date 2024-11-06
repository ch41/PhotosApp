package com.example.photosapp.data.repository

import com.example.photosapp.common.utils.Resource
import com.example.photosapp.data.mapper.toDomain
import com.example.photosapp.data.mapper.toRequestBody
import com.example.photosapp.data.service.AuthenticationService
import com.example.photosapp.domain.model.auth.SignIn
import com.example.photosapp.domain.model.auth.SignInResponse
import com.example.photosapp.domain.model.auth.SignUp
import com.example.photosapp.domain.model.auth.SignUpResponse
import com.example.photosapp.domain.repository.AuthenticationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val authenticationService: AuthenticationService
) : AuthenticationRepository {

    override fun signInUser(signIn: SignIn): Flow<Resource<SignInResponse>> =
        flow {
            emit(Resource.Loading)
            val response = authenticationService.signInUser(signIn.toRequestBody()).toDomain()
            emit(Resource.Success(response))

        }.catch { error ->
            when (error) {
                is HttpException -> emit(Resource.Error(message = "The password must contain from 4 to 32 characters, including Latin letters, numbers or symbols"))
                is IOException -> emit(Resource.Error("Network error, please check your connection"))
                else -> emit(Resource.Error(error.localizedMessage ?: "An unexpected error"))
            }

        }.flowOn(Dispatchers.IO)


    override fun signUpUser(signUp: SignUp): Flow<Resource<SignUpResponse>> =
        flow {
            emit(Resource.Loading)
            val response = authenticationService.signUpUser(signUp.toRequestBody()).toDomain()
            emit(Resource.Success(response))

        }.catch { error ->
            when (error) {
                is HttpException -> emit(Resource.Error("User already exist"))
                is IOException -> emit(Resource.Error("Network error, please check your connection"))
                else -> emit(Resource.Error(error.localizedMessage ?: "An unexpected error"))
            }
        }.flowOn(Dispatchers.IO)
}