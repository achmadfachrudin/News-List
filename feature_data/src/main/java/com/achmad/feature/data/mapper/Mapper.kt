package com.achmad.feature.data.mapper

import com.achmad.feature.data.entity.ArticleEntity
import com.achmad.feature.data.entity.NewsResponseEntity
import com.achmad.feature.data.entity.SourceEntity
import com.achmad.feature.data.model.Article
import com.achmad.feature.data.model.NewsResponse
import com.achmad.feature.data.model.Source

fun NewsResponseEntity.mapToNewsResponse(): NewsResponse {
    return NewsResponse(
        status = status.orEmpty(),
        totalResults = totalResults ?: 0,
        articles = articles?.map { it.mapToArticle() } ?: emptyList(),
        sources = sources?.map { it.mapToSource() } ?: emptyList()
    )
}

fun SourceEntity.mapToSource(): Source {
    return Source(
        id = id.orEmpty(),
        name = name.orEmpty(),
        description = description.orEmpty(),
        url = url.orEmpty(),
        category = category.orEmpty(),
        language = language.orEmpty(),
        country = country.orEmpty()
    )
}

fun ArticleEntity.mapToArticle(): Article {
    return Article(
        title = title.orEmpty(),
        author = author.orEmpty(),
        description = description.orEmpty(),
        url = url.orEmpty(),
        publishedAt = publishedAt.orEmpty()
    )
}
