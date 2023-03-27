package com.example.guessme.data.repository

import com.example.guessme.data.api.RetrofitApi
import com.example.guessme.data.model.InfoList
import com.example.guessme.data.response.BaseResponseBody
import com.example.guessme.domain.repository.PersonDetailRepository
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonDetailRepositoryImpl @Inject constructor(
    private val api: RetrofitApi
): PersonDetailRepository {

    override suspend fun addInfo(token: String, id: Int, info: InfoList): Response<BaseResponseBody> {
        return api.addInfo(token, id, info)
    }

}