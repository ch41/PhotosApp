package com.example.photosapp.data.service

import com.example.photosapp.data.model.auth.SignInBody
import com.example.photosapp.data.model.auth.SignInResponseBody
import com.example.photosapp.data.model.auth.SignUpBody
import com.example.photosapp.data.model.auth.SignUpResponseBody
import retrofit2.http.Body
import retrofit2.http.POST


interface AuthenticationService {

    @POST("/api/account/signin")
    suspend fun signInUser(@Body signInBody: SignInBody) : SignInResponseBody

    @POST("/api/account/signup")
    suspend fun signUpUser(@Body signUpBody: SignUpBody) : SignUpResponseBody

}