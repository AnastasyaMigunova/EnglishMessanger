package com.example.application.presentation.user.chats.dialog

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.application.data.model.ChatMessage

private const val SENDER_MESSAGE = 0
private const val RECIPIENT_MESSAGE = 1

class ItemsAdapter(private val senderUsername: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<ChatMessage>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            SENDER_MESSAGE -> SenderMessageViewHolder(parent, parent.context)
            RECIPIENT_MESSAGE -> RecipientMessageViewHolder(parent, parent.context)
            else -> error("ViewType not supported")
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SenderMessageViewHolder -> {
                holder.bind(items[position], position)
            }

            is RecipientMessageViewHolder -> {
                holder.bind(items[position], position)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position].sender == senderUsername) {
            SENDER_MESSAGE
        } else {
            RECIPIENT_MESSAGE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(infoItems: List<ChatMessage>) {
        this.items.clear()
        this.items.addAll(infoItems)
        notifyDataSetChanged()
    }
}
