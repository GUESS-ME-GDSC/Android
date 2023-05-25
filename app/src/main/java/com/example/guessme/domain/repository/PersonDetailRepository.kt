package com.example.guessme.domain.repository

import com.example.guessme.data.model.IdList
import com.example.guessme.data.model.InfoList
import com.example.guessme.data.response.BaseNullResponseBody
import com.example.guessme.data.response.PersonResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

interface PersonDetailRepository {
    suspend fun addInfo(token: String, id: Int, info: InfoList): Response<BaseNullResponseBody>

    suspend fun getPerson(token: String, id: Int): Response<PersonResponse>

    suspend fun deleteInfo(token: String, info: IdList): Response<BaseNullResponseBody>

    suspend fun modifyInfo(token: String, info: MultipartBody.Part, userId: Int): Response<BaseNullResponseBody>
}