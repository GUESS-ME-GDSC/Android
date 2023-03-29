package com.example.guessme.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class InfoList(
    @field:Json(name = "info") val data: List<Info>
): Parcelable
