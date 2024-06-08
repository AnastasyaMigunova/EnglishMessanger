package com.example.application.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Card (
    val id: Long? = null,
    val setId: Long,
    val text: String,
    val explanation: String,
    val userEmail: String
): Parcelable