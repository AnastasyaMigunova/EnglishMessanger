package com.example.application.presentation.user.chats.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.application.R
import com.example.application.data.model.ChatMessage
import com.example.application.databinding.ItemRecipientMessageBinding
import com.example.application.databinding.ItemSenderMessageBinding

class RecipientMessageViewHolder(
    parent: ViewGroup,
    val context: Context,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_recipient_message, parent, false),
) {
    private val binding by viewBinding(ItemRecipientMessageBinding::bind)

    fun bind(chatMessage: ChatMessage, position: Int) = with(binding) {
        binding.textViewItem.text = chatMessage.content
    }
}