package com.example.guessme.domain.repository

import com.example.guessme.data.response.QuizResponseBody
import retrofit2.Response

interface StartQuizRepository {
    suspend fun getPersonQuiz(token: String, id: Int): Response<QuizResponseBody>
}