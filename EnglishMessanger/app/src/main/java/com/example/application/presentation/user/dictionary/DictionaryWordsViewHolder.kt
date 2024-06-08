package com.example.application.presentation.user.dictionary

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.application.R
import com.example.application.data.model.Card
import com.example.application.data.model.Word
import com.example.application.databinding.ItemWordBinding
import com.example.application.`interface`.OnDictionaryWordListener

class DictionaryWordsViewHolder(
    parent: ViewGroup,
    private val context: Context,
    private val listener: OnDictionaryWordListener
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_word, parent, false),
) {
    private val binding by viewBinding(ItemWordBinding::bind)

    fun bind(word: Word, position: Int) = with(binding) {
        val wordEng = binding.textViewEng
        val wordRus = binding.textViewRus

        binding.imageViewItem.visibility = View.GONE
        binding.imageViewSound.setImageResource(R.mipmap.icon_add_symbol_foreground)
        wordEng.text = word.word
        wordRus.text = word.description

        binding.imageViewSound.setOnClickListener {
            listener.onItemWordClick(word)
            binding.imageViewSound.setImageResource(R.mipmap.icon_checkmark_foreground)
        }
    }
}