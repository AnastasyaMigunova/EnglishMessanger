package com.example.application.data.model

sealed class ListItem {
    data class TopicItem(val topic: Topic) : ListItem()

    data class TheoryItem(val theory: Theory) : ListItem()

    data class SubtopicItem(val subtopic: Subtopic) : ListItem()

    data class CategoryItem(val category: Category) : ListItem()

}
