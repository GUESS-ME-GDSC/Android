package com.example.guessme.data.repository

import com.example.guessme.data.model.User
import okhttp3.ResponseBody
import retrofit2.Response

interface LogInRepository {
    suspend fun logIn(user: User): Response<ResponseBody>
    suspend fun signUp(user: User): Response<ResponseBody>
}