package com.example.guessme.data.repository

import com.example.guessme.data.api.RetrofitApi
import com.example.guessme.data.response.PersonResponse
import com.example.guessme.domain.repository.ScoreRepository
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScoreRepositoryImpl @Inject constructor(
    private val api: RetrofitApi
): ScoreRepository {
    override suspend fun getPerson(token: String, id: Int): Response<PersonResponse> {
        return api.getPerson(token, id)
    }
}