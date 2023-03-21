package com.example.guessme.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Info(
    val uuid: Int?,
    val owner: Int,
    val infoKey: String,
    val infoValue: String,
): Parcelable