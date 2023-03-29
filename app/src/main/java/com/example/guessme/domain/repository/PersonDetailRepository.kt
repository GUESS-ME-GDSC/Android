package com.example.guessme.domain.repository

import com.example.guessme.data.model.InfoList
import com.example.guessme.data.response.BaseNullResponseBody
import com.example.guessme.data.response.BaseResponseBody
import com.example.guessme.data.response.PersonResponse
import retrofit2.Response

interface PersonDetailRepository {
    suspend fun addInfo(token: String, id: Int, info: InfoList): Response<BaseNullResponseBody>

    suspend fun getPerson(token: String, id: Int): Response<PersonResponse>
}