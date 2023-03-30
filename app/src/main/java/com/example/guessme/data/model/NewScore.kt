package com.example.guessme.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewScore(
    val personId: Int,
    val score: Int
)