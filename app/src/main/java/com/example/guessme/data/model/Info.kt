package com.example.guessme.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Info(
    val uuid: Int?,
    @field:Json(name = "infoKey") val infoKey: String,
    @field:Json(name = "infoValue") val infoValue: String,
): Parcelable