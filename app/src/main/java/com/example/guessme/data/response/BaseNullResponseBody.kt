package com.example.guessme.data.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BaseNullResponseBody(
    val status: Int,
    val message: String,
    val data: String?
)
