package com.example.guessme.data.repository

import com.example.guessme.data.api.RetrofitApi
import com.example.guessme.data.response.QuizResponseBody
import com.example.guessme.domain.repository.StartQuizRepository
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StartQuizRepositoryImpl @Inject constructor(
    private val api: RetrofitApi
): StartQuizRepository {
    override suspend fun getPersonQuiz(token: String, id: Int): Response<QuizResponseBody> {
        return api.getPersonQuiz(token, id)
    }
}