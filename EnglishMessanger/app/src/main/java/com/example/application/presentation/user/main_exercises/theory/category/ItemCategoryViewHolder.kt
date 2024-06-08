package com.example.application.presentation.user.main_exercises.theory.category

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.application.R
import com.example.application.data.model.Category
import com.example.application.databinding.ItemTheoryBinding
import com.example.application.`interface`.OnTheoryItemClick

class ItemCategoryViewHolder(
    parent: ViewGroup,
    private val context: Context,
    private val listener: OnTheoryItemClick,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_theory, parent, false),
) {
    private val binding by viewBinding(ItemTheoryBinding::bind)

    fun bind(category: Category, position: Int) = with(binding) {
        val title = binding.textViewItemName
        val description = binding.textViewItemDescription
        val topics = binding.numberTopics

        topics.visibility = View.VISIBLE
        title.text = category.title
        description.text = category.description
        topics.text = buildString {
            append(category.topics.size.toString())
            append(" topics")
        }
    }
}