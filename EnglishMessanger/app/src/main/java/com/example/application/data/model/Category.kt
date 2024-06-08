package com.example.application.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val id: Long? = null,
    val title: String,
    val description: String,
    val topics: List<Topic>,
    val theoryList: List<Theory>
) : Parcelable
