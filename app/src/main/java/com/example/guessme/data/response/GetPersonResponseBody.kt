package com.example.guessme.data.response

import com.example.guessme.data.model.Person
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetPersonResponseBody(
    val status: Int,
    val message: String,
    val data: Person
)
