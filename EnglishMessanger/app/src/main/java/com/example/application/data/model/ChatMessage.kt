package com.example.application.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class ChatMessage(
    val id: Long? = null,
    val idChat: String? = "",
    val type: String,
    val content: String,
    val sender: String,
    val recipient: String,
    val senderId: String,
    val recipientId: String,
    val date: Date? = null,
    val isRead: Boolean? = false
): Parcelable