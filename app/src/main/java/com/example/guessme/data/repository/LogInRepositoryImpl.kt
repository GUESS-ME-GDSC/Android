package com.example.guessme.data.repository

import com.example.guessme.data.api.RetrofitApi
import com.example.guessme.data.model.User
import com.example.guessme.data.response.BaseResponseBody
import com.example.guessme.domain.repository.LogInRepository
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogInRepositoryImpl @Inject constructor(
    private val api: RetrofitApi
): LogInRepository {
    override suspend fun logIn(user: User): Response<BaseResponseBody> = api.logIn(user)
}