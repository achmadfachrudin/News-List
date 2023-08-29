package com.achmad.feature.data.model

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>,
    val sources: List<Source>,
)
