package com.example.application.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Theory (
    val id: Long,
    val title: String,
    val level: String,
    val explanation: String,
    val example: String,
    val commonMistakeDescription: String,
    val cmWrong: String,
    val cmRight: String
) : Parcelable