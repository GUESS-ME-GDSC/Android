package com.example.guessme.domain.repository

import com.example.guessme.data.response.BaseNullResponseBody
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

interface ModifyPersonRepository {
    suspend fun deletePerson(token: String, id: Int): Response<BaseNullResponseBody>

    suspend fun modifyPerson(token: String, id: Int, name: RequestBody, relation: RequestBody, birth: RequestBody, residence: RequestBody, image: MultipartBody.Part?, voice: MultipartBody.Part?): Response<BaseNullResponseBody>

    suspend fun modifyPersonFavorite(token: String, id: Int): Response<BaseNullResponseBody>
}