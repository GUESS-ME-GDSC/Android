package com.example.guessme.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class User(
    val uuid: Int?,
    @field:Json(name = "userId") val id: String,
    @field:Json(name = "userPassword") val pw: String
): Parcelable
