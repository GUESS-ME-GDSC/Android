package com.example.guessme.data.model

import okhttp3.MultipartBody
import java.time.LocalDateTime

data class Person(
    val uuid: Int?,
    val favorite: Boolean,
    val image: MultipartBody.Part?,
    val voice: MultipartBody.Part?,
    val info: List<Info>?,
    val name: String,
    val relation: String,
    val birth: LocalDateTime,
    val residence: String,
    val score: Int?
)
