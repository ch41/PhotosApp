package com.example.photosapp.domain.use_case

import com.example.photosapp.common.extensions.isValidLogin
import com.example.photosapp.common.extensions.isValidPassword
import com.example.photosapp.common.utils.Resource
import com.example.photosapp.domain.model.auth.SignUp
import com.example.photosapp.domain.model.auth.SignUpResponse
import com.example.photosapp.domain.repository.AuthenticationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SignUpUserUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
) {
    operator fun invoke(
        signUpCredentials: SignUp
    ): Flow<Resource<SignUpResponse>> = flow {

        val loginValid = signUpCredentials.login.isValidLogin()
        val passwordValid = signUpCredentials.password.isValidPassword()

        if (!loginValid) {
            emit(Resource.Error("Incorrect login"))
            return@flow
        }

        if (!passwordValid) {
            emit(Resource.Error("Incorrect password"))
            return@flow
        }

        val passwordsMatch = signUpCredentials.password == signUpCredentials.repeatPassword

        if(!passwordsMatch) {
            emit(Resource.Error("Passwords don't match"))
            return@flow
        }

        emit(Resource.Loading)

        authenticationRepository.signUpUser(signUpCredentials).collect { resource ->

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