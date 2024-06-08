package com.example.application.presentation.user.main_exercises.theory.subtopic

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.application.R
import com.example.application.data.model.Subtopic
import com.example.application.databinding.ItemTheoryBinding
import com.example.application.`interface`.OnTheoryItemClick

class ItemSubtopicViewHolder(
    parent: ViewGroup,
    private val context: Context,
    private val listener: OnTheoryItemClick,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_theory, parent, false),
) {
    private val binding by viewBinding(ItemTheoryBinding::bind)

    fun bind(subtopic: Subtopic, position: Int) = with(binding) {
        val title = binding.textViewItemName
        val description = binding.textViewItemDescription
        val topics = binding.numberTopics

        binding.cardViewItemTheory.setCardBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.background
            )
        )
        topics.visibility = View.GONE
        title.text = subtopic.title
        description.text = subtopic.description
    }
}