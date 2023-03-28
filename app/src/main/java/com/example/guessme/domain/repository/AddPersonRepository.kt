package com.example.guessme.domain.repository

import com.example.guessme.data.model.Person
import com.example.guessme.data.response.AddPersonRequestBody
import retrofit2.Response
import java.io.File

interface AddPersonRepository {
    suspend fun addPerson(token: String, person: Person, image: File?): Response<AddPersonRequestBody>
}