package com.example.guessme.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IdList(
    val list: List<Int>
)
