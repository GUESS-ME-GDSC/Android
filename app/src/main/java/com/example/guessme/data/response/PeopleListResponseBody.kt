package com.example.guessme.data.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PeopleListResponseBody(
    @field:Json(name = "data")
    val `data`: List<PersonPreview>,
    @field:Json(name = "message")
    val message: String,
    @field:Json(name = "status")
    val status: Int
)