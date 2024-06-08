package com.example.application.presentation.user.main_exercises.cards.adding_word

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.application.data.model.CardSet
import com.example.application.data.model.SentenceExercise
import com.example.application.`interface`.OnItemClickListener

class ItemsAdapter : RecyclerView.Adapter<CardSetsViewHolder>() {

    private val items = mutableListOf<CardSet>()
    lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardSetsViewHolder {
        return CardSetsViewHolder(parent, parent.context, listener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CardSetsViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(infoItems: List<CardSet>) {
        this.items.clear()
        this.items.addAll(infoItems)
        notifyDataSetChanged()
    }
}
