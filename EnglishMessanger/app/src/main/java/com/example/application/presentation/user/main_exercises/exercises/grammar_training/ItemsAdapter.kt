package com.example.application.presentation.user.main_exercises.exercises.grammar_training

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.application.data.model.SentenceExercise
import com.example.application.`interface`.OnItemClickListener

class ItemsAdapter() :
    RecyclerView.Adapter<GrammarTrainingViewHolder>() {

    private val items = mutableListOf<SentenceExercise>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GrammarTrainingViewHolder {
        return GrammarTrainingViewHolder(parent, parent.context)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: GrammarTrainingViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(infoItems: List<SentenceExercise>) {
        this.items.clear()
        this.items.addAll(infoItems)
        notifyDataSetChanged()
    }
}
