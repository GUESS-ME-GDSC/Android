package com.example.guessme.domain.repository

import com.example.guessme.data.response.ScoringResponseBody
import retrofit2.Response

interface ScoringRepository {
    suspend fun quizScoring(token: String, image: String, infoValue: String): Response<ScoringResponseBody>
}