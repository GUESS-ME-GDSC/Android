package com.example.guessme.data.repository

import com.example.guessme.data.api.RetrofitApi
import com.example.guessme.data.response.PeopleListResponseBody
import com.example.guessme.domain.repository.PeopleListRepository
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PeopleListRepositoryImpl @Inject constructor(
    private val api: RetrofitApi
): PeopleListRepository {
    override suspend fun getPeopleList(token: String, favorite: Boolean): Response<PeopleListResponseBody> {
        return api.getPeopleList(token, favorite)
    }
}