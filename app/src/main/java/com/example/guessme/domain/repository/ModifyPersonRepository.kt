package com.example.guessme.domain.repository

import com.example.guessme.data.response.BaseNullResponseBody
import retrofit2.Response

interface ModifyPersonRepository {
    suspend fun deletePerson(token: String, id: Int): Response<BaseNullResponseBody>
}