package com.example.guessme.domain.repository

import com.example.guessme.data.model.NewScore
import com.example.guessme.data.response.BaseResponseBody
import com.example.guessme.data.response.PersonResponse
import retrofit2.Response

interface ScoreRepository {
    suspend fun getPerson(token: String, id: Int): Response<PersonResponse>

    suspend fun patchNewScore(token: String, newScore: NewScore): Response<BaseResponseBody>
}