package com.example.guessme.domain.repository

import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    suspend fun saveToken(token: String)

    suspend fun getToken(): Flow<String>
}