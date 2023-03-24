package com.example.guessme.data.repository

import com.example.guessme.common.util.RetrofitInstance.api
import com.example.guessme.data.model.User
import com.example.guessme.data.response.LoginResponseBody
import com.example.guessme.domain.repository.LogInRepository
import retrofit2.Response

class LogInRepositoryImpl: LogInRepository {
    override suspend fun logIn(user: User): Response<LoginResponseBody> = api.logIn(user)
}