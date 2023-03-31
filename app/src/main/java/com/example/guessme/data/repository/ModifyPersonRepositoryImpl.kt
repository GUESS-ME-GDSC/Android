package com.example.guessme.data.repository

import com.example.guessme.data.api.RetrofitApi
import com.example.guessme.data.response.BaseNullResponseBody
import com.example.guessme.domain.repository.ModifyPersonRepository
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
}