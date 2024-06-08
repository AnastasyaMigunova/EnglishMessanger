package com.example.application.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Topic (
    val id: Long? = null,
    val title: String,
    val description: String,
    val theoryList: List<Theory>,
    val subtopicList: List<Subtopic>
) : Parcelable