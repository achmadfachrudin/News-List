package com.achmad.feature.data.entity

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class ArticleEntity(
    @field:Json(name = "title") val title: String?,
    @field:Json(name = "author") val author: String?,
    @field:Json(name = "description") val description: String?,
    @field:Json(name = "url") val url: String?,
    @field:Json(name = "publishedAt") val publishedAt: String?,
)
