package com.example.guessme.data.response

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Quiz(
    @field:Json(name = "answer")
    val answer: String,
    @field:Json(name = "question")
    val question: String
): Parcelable