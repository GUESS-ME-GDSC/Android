package com.example.guessme.data.api

import com.example.guessme.data.model.User
import com.example.guessme.data.response.LoginResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitApi {
    @POST("/auth/login")
    suspend fun logIn(
        @Body user: User
    ): Response<LoginResponseBody>

    @POST("/auth/join")
    suspend fun signUp(
        @Body user: User
    ): Response<LoginResponseBody>
}