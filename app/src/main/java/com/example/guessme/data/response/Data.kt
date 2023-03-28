package com.example.guessme.data.response


import com.example.guessme.data.model.Info
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Data(
    @field:Json(name = "birth")
    val birth: String,
    @field:Json(name = "favorite")
    val favorite: Boolean,
    @field:Json(name = "image")
    val image: String,
    @field:Json(name = "info")
    val info: List<Info>,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "relation")
    val relation: String,
    @field:Json(name = "residence")
    val residence: String,
    @field:Json(name = "voice")
    val voice: String
)