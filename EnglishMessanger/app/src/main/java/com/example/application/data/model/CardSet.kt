package com.example.application.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CardSet(
    val id: Long? = null,
    val title: String,
    val description: String,
    val userEmail: String,
    val cardList: List<Card>? = null,
    val toLearn: List<Card>? = null,
    val learned: List<Card>? = null,
    var selected: Boolean = false
) : Parcelable
