package com.example.application.presentation.user.main_exercises.cards.studying

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.application.R
import com.example.application.data.model.Card
import com.example.application.databinding.ItemWordBinding

class StudyingWordsViewHolder(
    parent: ViewGroup,
    private val context: Context,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_word, parent, false),
) {
    private val binding by viewBinding(ItemWordBinding::bind)

    fun bind(card: Card, position: Int) = with(binding) {
        val wordEng = binding.textViewEng
        val wordRus = binding.textViewRus

        binding.imageViewItem.visibility = View.GONE
        wordEng.text = card.text
        wordRus.text = card.explanation
    }
}