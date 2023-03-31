package com.example.guessme.domain.repository

import com.example.guessme.data.model.User
import com.example.guessme.data.response.BaseNullResponseBody
import retrofit2.Response

interface SignUpRepository {
    suspend fun signUp(user: User): Response<BaseNullResponseBody>
}