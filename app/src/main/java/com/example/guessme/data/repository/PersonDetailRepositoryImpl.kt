package com.example.guessme.data.repository

import com.example.guessme.data.api.RetrofitApi
import com.example.guessme.data.model.IdList
import com.example.guessme.data.model.InfoList
import com.example.guessme.data.response.BaseNullResponseBody
import com.example.guessme.data.response.PersonResponse
import com.example.guessme.domain.repository.PersonDetailRepository
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonDetailRepositoryImpl @Inject constructor(
    private val api: RetrofitApi
): PersonDetailRepository {

    override suspend fun addInfo(token: String, id: Int, info: InfoList): Response<BaseNullResponseBody> {
        return api.addInfo(token, id, info)
    }

    override suspend fun getPerson(token: String, id: Int): Response<PersonResponse> {
        return api.getPerson(token, id)
    }

    override suspend fun deleteInfo(token: String, info: IdList): Response<BaseNullResponseBody> {
        return api.deleteInfo(token, info)
    }

    override suspend fun modifyInfo(token: String, info: InfoList, userId: Int): Response<BaseNullResponseBody> {
        return api.modifyInfo(token, userId, null, null,  info, null, null, null, null)
    }

}