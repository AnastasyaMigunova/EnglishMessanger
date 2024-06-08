package com.example.application.presentation.onboarding.interests

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.application.R
import com.example.application.data.model.Interest
import com.example.application.databinding.ItemInterestBinding
import com.example.application.`interface`.OnItemClickListener
import kotlin.random.Random

class InterestsViewHolder(
    parent: ViewGroup,
    private val context: Context,
    private val listener: OnItemClickListener,
    private val viewModel: InterestsViewModel
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_interest, parent, false),
) {
    private val binding by viewBinding(ItemInterestBinding::bind)
    private val selectedPositions = mutableSetOf<String>()

    fun bind(interest: Interest, position: Int) = with(binding) {
        val strPos = position.toString()
        val cardView = binding.cardViewInterest
        val textView = binding.textViewInterest

        if (interest.select) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.start_text))
            textView.setTextColor(ContextCompat.getColor(context, R.color.white))
        }
        else {
            randomColor(cardView, textView)
        }
        binding.cardViewInterest.setOnClickListener {
            listener.onItemClick(position)
            if (selectedPositions.contains(strPos)) {
                randomColor(cardView, textView)
                interest.select = false
                selectedPositions.remove(strPos)
                viewModel.removeItem(position)
            } else {
                cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.start_text))
                textView.setTextColor(ContextCompat.getColor(context, R.color.white))
                interest.select = true
                selectedPositions.add(strPos)
                viewModel.addItem(interest)
            }
        }

        textView.text = interest.interest
    }

    private fun randomColor(cardView: CardView, textView: TextView) {
        if (Random.nextBoolean()) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.border))
            textView.setTextColor(ContextCompat.getColor(context, R.color.background))
        } else {
            cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
            textView.setTextColor(ContextCompat.getColor(context, R.color.start_text))
        }
    }
}