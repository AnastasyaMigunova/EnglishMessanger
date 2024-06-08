package com.example.application.data.model

data class Match (
    val message: String? = null,
    val replacements: List<Replacement>? = null,
    var offset: Int,
    var length: Int,
    val rule: Rule? = null
)