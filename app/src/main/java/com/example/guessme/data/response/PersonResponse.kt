package com.example.guessme.data.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PersonResponse(
    @field:Json(name = "data")
    val `data`: Data,
    @field:Json(name = "message")
    val message: String,
    @field:Json(name = "status")
    val status: Int
)