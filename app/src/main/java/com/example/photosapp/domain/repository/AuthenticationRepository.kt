package com.example.photosapp.domain.repository

import com.example.photosapp.common.utils.Resource
import com.example.photosapp.domain.model.auth.SignIn
import com.example.photosapp.domain.model.auth.SignInResponse
import com.example.photosapp.domain.model.auth.SignUp
import com.example.photosapp.domain.model.auth.SignUpResponse
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {

    fun signInUser(
        signIn: SignIn
    ) : Flow<Resource<SignInResponse>>

    fun signUpUser(
        signUp: SignUp
    ) : Flow<Resource<SignUpResponse>>

}