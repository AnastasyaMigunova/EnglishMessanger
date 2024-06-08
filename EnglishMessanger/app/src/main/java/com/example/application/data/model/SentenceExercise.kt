package com.example.application.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SentenceExercise(
    val exercise: String,
    val right_answer: String,
    var current_answer: String? = null
) : Parcelable
