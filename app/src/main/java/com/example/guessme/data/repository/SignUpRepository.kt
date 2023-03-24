package com.example.guessme.data.repository

import com.example.guessme.common.util.RetrofitInstance
import com.example.guessme.data.model.User
import com.example.guessme.data.response.LoginResponseBody
import com.example.guessme.domain.repository.SignUpRepository
import retrofit2.Response

class SignUpRepositoryImpl: SignUpRepository{
    override suspend fun signUp(user: User): Response<LoginResponseBody> = RetrofitInstance.api.signUp(user)
}