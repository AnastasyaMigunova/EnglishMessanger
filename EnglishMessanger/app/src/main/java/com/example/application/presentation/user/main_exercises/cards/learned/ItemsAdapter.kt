package com.example.application.presentation.user.main_exercises.cards.learned

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.application.data.model.Card

class ItemsAdapter : RecyclerView.Adapter<LearnedWordsViewHolder>() {

    private val items = mutableListOf<Card>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LearnedWordsViewHolder {
        return LearnedWordsViewHolder(parent, parent.context)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: LearnedWordsViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(infoItems: List<Card>) {
        this.items.clear()
        this.items.addAll(infoItems)
        notifyDataSetChanged()
    }
}
