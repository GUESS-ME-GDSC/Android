package com.example.guessme.data.model

import android.net.Uri
import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class Person(
    val uuid: Int?,
    var favorite: Boolean,
    val image: Uri?,
    val voice: Uri?,
    val info: InfoList?,
    val name: String,
    val relation: String,
    val birth: LocalDateTime,
    val residence: String,
    val score: Int?
): Parcelable
