package com.example.application.presentation.user.dictionary

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.application.data.model.Word
import com.example.application.`interface`.OnDictionaryWordListener

class ItemsAdapter : RecyclerView.Adapter<DictionaryWordsViewHolder>() {

    private val items = mutableListOf<Word>()
    lateinit var listener: OnDictionaryWordListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DictionaryWordsViewHolder {
        return DictionaryWordsViewHolder(parent, parent.context, listener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: DictionaryWordsViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(infoItems: List<Word>) {
        this.items.clear()
        this.items.addAll(infoItems)
        notifyDataSetChanged()
    }
}
