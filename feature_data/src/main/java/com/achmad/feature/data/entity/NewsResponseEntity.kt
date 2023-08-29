package com.achmad.feature.data.entity

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class NewsResponseEntity(
    @field:Json(name = "status") val status: String?,
    @field:Json(name = "totalResults") val totalResults: Int?,
    @field:Json(name = "articles") val articles: List<ArticleEntity>?,
    @field:Json(name = "sources") val sources: List<SourceEntity>?,
)
