package com.example.arplusplu_task.ui.models

import com.example.arplusplu_task.ui.models.Article

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)