package com.example.application.`interface`

import com.example.application.data.model.CardSet
import com.example.application.data.model.Category
import com.example.application.data.model.ListItem

interface OnItemClickListener {
    fun onItemClick(position: Int)
    fun onItemSetClick(cardSet: CardSet)
    fun onItemExerciseClick(rightAnswer: String, currentAnswer: String)
}