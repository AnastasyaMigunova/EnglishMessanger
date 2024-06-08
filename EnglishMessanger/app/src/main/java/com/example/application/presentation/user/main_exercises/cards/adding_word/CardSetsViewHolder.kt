package com.example.application.presentation.user.main_exercises.cards.adding_word

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.application.R
import com.example.application.data.model.CardSet
import com.example.application.databinding.ItemSetBinding
import com.example.application.databinding.ItemSetHorizontalBinding
import com.example.application.`interface`.OnItemClickListener

class CardSetsViewHolder(
    parent: ViewGroup,
    private val context: Context,
    private val listener: OnItemClickListener,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_set_horizontal, parent, false),
) {
    private val binding by viewBinding(ItemSetHorizontalBinding::bind)

    fun bind(cardSet: CardSet, position: Int) = with(binding) {
        val title = binding.textViewSetTitle
        val description = binding.textViewSetDescription

        title.text = cardSet.title
        description.text = cardSet.description

        binding.cardViewSet.setOnClickListener {
            if (cardSet.selected) {
                cardSet.selected = false
                binding.cardViewSet.setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.border
                    )
                )
            } else {
                cardSet.selected = true
                binding.cardViewSet.setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.start_text
                    )
                )
                listener.onItemSetClick(cardSet)
            }
        }
    }
}