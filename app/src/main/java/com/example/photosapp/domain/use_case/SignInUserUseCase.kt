package com.example.photosapp.domain.use_case

import com.example.photosapp.common.extensions.isValidLogin
import com.example.photosapp.common.extensions.isValidPassword
import com.example.photosapp.common.utils.Resource
import com.example.photosapp.domain.model.auth.SignIn
import com.example.photosapp.domain.model.auth.SignInResponse
import com.example.photosapp.domain.repository.AuthenticationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SignInUserUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
) {
    operator fun invoke(
        signInCredentials: SignIn
    ): Flow<Resource<SignInResponse>> = flow {

        val loginValid = signInCredentials.login.isValidLogin()
        val passwordValid = signInCredentials.password.isValidPassword()

        if (!loginValid) {
            emit(Resource.Error("Incorrect login"))
            return@flow
        }

        if (!passwordValid) {
            emit(Resource.Error("Incorrect password"))
            return@flow
        }

        emit(Resource.Loading)

        authenticationRepository.signInUser(signInCredentials).collect { resource ->

            when (resource) {
                is Resource.Success -> {
                    emit(Resource.Success(resource.data))
                }

                is Resource.Error -> {
                    emit(Resource.Error(resource.message))
                }

                is Resource.Loading -> {
                    emit(Resource.Loading)
                }
            }
        }

    }.flowOn(Dispatchers.IO)

}