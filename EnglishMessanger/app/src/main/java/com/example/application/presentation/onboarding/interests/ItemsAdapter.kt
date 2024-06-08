package com.example.application.presentation.onboarding.interests

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.application.data.model.Interest
import com.example.application.`interface`.OnItemClickListener

class ItemsAdapter(private val viewModel: InterestsViewModel) : RecyclerView.Adapter<InterestsViewHolder>() {

    private val items = mutableListOf<Interest>()
    lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InterestsViewHolder {
        return InterestsViewHolder(parent, parent.context, listener, viewModel)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: InterestsViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(infoItems: List<Interest>) {
        this.items.clear()
        this.items.addAll(infoItems)
        notifyDataSetChanged()
    }
}
