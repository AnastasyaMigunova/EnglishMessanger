package com.example.application.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatRoom(
    val id: Long? = null,
    val senderId: String,
    val recipientId: String,
    val chatId: String,
    var lastMessage: String? = null,
    var chatMessageList: List<ChatMessage> = mutableListOf()
): Parcelable
