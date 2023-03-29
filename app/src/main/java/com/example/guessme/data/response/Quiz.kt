package com.example.guessme.data.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Quiz(
    @field:Json(name = "answer")
    val answer: String,
    @field:Json(name = "question")
    val question: String
)