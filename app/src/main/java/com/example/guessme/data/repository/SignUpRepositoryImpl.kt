package com.example.guessme.data.repository

import com.example.guessme.data.api.RetrofitApi
import com.example.guessme.data.model.User
import com.example.guessme.data.response.BaseNullResponseBody
import com.example.guessme.domain.repository.SignUpRepository
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignUpRepositoryImpl @Inject constructor(
    private val api: RetrofitApi
): SignUpRepository{
    override suspend fun signUp(user: User): Response<BaseNullResponseBody> = api.signUp(user)
}