package com.example.guessme.data.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class Person(
    val uuid: Int?,
    var favorite: Boolean,
    val image: Uri?,
    val voice: Uri?,
    val info: InfoList?,
    val name: String,
    val relation: String,
    val birth: LocalDate,
    val residence: String,
    val score: Int?
): Parcelable
