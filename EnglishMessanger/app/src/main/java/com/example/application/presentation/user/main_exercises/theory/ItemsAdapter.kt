package com.example.application.presentation.user.main_exercises.theory

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.application.data.model.ListItem
import com.example.application.`interface`.OnTheoryItemClick
import com.example.application.presentation.user.main_exercises.theory.topic.ItemTopicViewHolder
import com.example.application.presentation.user.main_exercises.theory.category.ItemCategoryViewHolder
import com.example.application.presentation.user.main_exercises.theory.theory_list.ItemTheoryListViewHolder

private const val TYPE_TOPIC = 0
private const val TYPE_THEORY_LIST = 1
private const val TYPE_SUBTOPIC = 2
private const val TYPE_CATEGORY = 3

class ItemsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<ListItem>()
    lateinit var listener: OnTheoryItemClick

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_CATEGORY -> ItemCategoryViewHolder(parent, parent.context, listener)
            TYPE_TOPIC -> ItemTopicViewHolder(parent, parent.context, listener)
            TYPE_THEORY_LIST -> ItemTheoryListViewHolder(parent, parent.context, listener)
            else -> error("ViewType not supported")
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ItemCategoryViewHolder -> {
                holder.bind((items[position] as ListItem.CategoryItem).category, position)
                holder.itemView.setOnClickListener {
                    listener.onCategoryClick((items[position] as ListItem.CategoryItem).category)
                }
            }

            is ItemTopicViewHolder -> {
                holder.bind((items[position] as ListItem.TopicItem).topic, position)
                holder.itemView.setOnClickListener {
                    listener.onTopicClick((items[position] as ListItem.TopicItem).topic)
                }
            }

            is ItemTheoryListViewHolder -> {
                holder.bind((items[position] as ListItem.TheoryItem).theory, position)
                holder.itemView.setOnClickListener {
                    listener.onTheoryListItemClick((items[position] as ListItem.TheoryItem).theory)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] is ListItem.CategoryItem) {
            TYPE_CATEGORY
        } else if (items[position] is ListItem.TopicItem) {
            TYPE_TOPIC
        } else if (items[position] is ListItem.SubtopicItem) {
            TYPE_SUBTOPIC
        } else {
            TYPE_THEORY_LIST
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(infoItems: List<ListItem>) {
        this.items.clear()
        this.items.addAll(infoItems)
        notifyDataSetChanged()
    }
}