package com.example.application.presentation.user.main_exercises.cards.sets

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.application.R
import com.example.application.data.model.CardSet
import com.example.application.databinding.ItemSetBinding
import com.example.application.`interface`.OnItemClickListener

class CardSetsViewHolder(
    parent: ViewGroup,
    private val context: Context,
    private val listener: OnItemClickListener,
    ) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_set, parent, false),
) {
    private val binding by viewBinding(ItemSetBinding::bind)

    fun bind(cardSet: CardSet, position: Int) = with(binding) {
        val title = binding.textViewTitle
        val description = binding.textViewDescription

        title.text = cardSet.title
        description.text = cardSet.description

        binding.cardViewSet.setOnClickListener {
            listener.onItemSetClick(cardSet)
        }
    }
}