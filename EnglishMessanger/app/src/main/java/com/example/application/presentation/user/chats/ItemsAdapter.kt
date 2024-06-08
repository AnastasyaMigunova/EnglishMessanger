package com.example.application.presentation.user.chats

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.application.data.model.User
import com.example.application.`interface`.OnItemClickListener

class ItemsAdapter() : RecyclerView.Adapter<FriendsChatsViewHolder>() {

    private val items = mutableListOf<User>()
    lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsChatsViewHolder {
        return FriendsChatsViewHolder(parent, parent.context, listener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: FriendsChatsViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(infoItems: List<User>) {
        this.items.clear()
        this.items.addAll(infoItems)
        notifyDataSetChanged()
    }
}
