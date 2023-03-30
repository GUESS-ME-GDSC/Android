package com.example.guessme.data.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PersonQuiz(
    @field:Json(name = "image")
    val image: String?,
    @field:Json(name = "quizList")
    val quizList: List<Quiz>,
    @field:Json(name = "voice")
    val voice: String?,
    @field:Json(name = "personId")
    val personId: Int,
    @field:Json(name = "score")
    val score: Int
)