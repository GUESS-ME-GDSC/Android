package com.example.guessme.domain.repository

import com.example.guessme.data.response.PeopleListResponseBody
import retrofit2.Response

interface PeopleListRepository {
    suspend fun getPeopleList(token: String, favorite: Boolean): Response<PeopleListResponseBody>
}