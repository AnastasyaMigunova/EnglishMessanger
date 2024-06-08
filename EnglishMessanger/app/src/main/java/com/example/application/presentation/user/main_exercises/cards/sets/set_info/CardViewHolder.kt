package com.example.application.presentation.user.main_exercises.cards.sets.set_info

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.application.R
import com.example.application.data.model.Card
import com.example.application.databinding.ItemSetHorizontalBinding
import com.example.application.`interface`.OnItemClickListener

class CardViewHolder(
    parent: ViewGroup,
    private val context: Context,
    private val listener: OnItemClickListener,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_set_horizontal, parent, false),
) {
    private val binding by viewBinding(ItemSetHorizontalBinding::bind)

    fun bind(card: Card, position: Int) = with(binding) {
        val title = binding.textViewSetTitle
        val description = binding.textViewSetDescription

        title.text = card.text
        description.text = card.explanation

        binding.cardViewSet.setOnClickListener {
            listener.onItemClick(position)
        }
    }
}