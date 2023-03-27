package com.example.guessme.domain.repository

import com.example.guessme.data.model.User
import com.example.guessme.data.response.BaseResponseBody
import retrofit2.Response

interface LogInRepository {
    suspend fun logIn(user: User): Response<BaseResponseBody>
}