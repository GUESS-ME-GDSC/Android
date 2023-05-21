package com.example.guessme.data.repository

import com.example.guessme.data.api.RetrofitApi
import com.example.guessme.data.response.BaseNullResponseBody
import com.example.guessme.domain.repository.ModifyPersonRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ModifyPersonRepositoryImpl @Inject constructor(
    private val api: RetrofitApi
): ModifyPersonRepository {
    override suspend fun deletePerson(token: String, id: Int): Response<BaseNullResponseBody> {
        return api.deletePerson(token, id)
    }

    override suspend fun modifyPerson(
        token: String,
        id: Int,
        name: RequestBody,
        relation: RequestBody,
        birth: RequestBody,
        residence: RequestBody,
        image: MultipartBody.Part?,
        voice: MultipartBody.Part?
    ): Response<BaseNullResponseBody> {
        return api.modifyPerson(token, id, image, voice, null, name, relation, birth, residence)
    }

    override suspend fun modifyPersonFavorite(
        token: String,
        id: Int
    ): Response<BaseNullResponseBody> {
        return api.modifyPersonFavorite(token, id)
    }


}