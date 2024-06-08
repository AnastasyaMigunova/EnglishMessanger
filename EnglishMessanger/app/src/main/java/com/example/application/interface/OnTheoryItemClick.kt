package com.example.application.`interface`

import com.example.application.data.model.Category
import com.example.application.data.model.ListItem
import com.example.application.data.model.Subtopic
import com.example.application.data.model.Theory
import com.example.application.data.model.Topic

interface OnTheoryItemClick {
    fun onCategoryClick(category: Category)
    fun onTopicClick(topic: Topic)
    fun onSubtopicClick(subtopic: Subtopic)
    fun onTheoryListItemClick(theory: Theory)
}