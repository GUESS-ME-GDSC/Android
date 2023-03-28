package com.example.guessme.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InfoList(
    val data: List<Info>
): Parcelable
