package com.achmad.base.service

import com.achmad.feature.data.entity.NewsResponseEntity
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject

class AppRemote @Inject constructor(
    private val service: AppService,
) {

    suspend fun fetchSourceByCategory(
        category: String,
    ): ApiResponse<NewsResponseEntity> = service.fetchSourceByCategory(category)

    suspend fun fetchArticleBySource(
        query: String,
        sources: String,
        page: Int,
    ): ApiResponse<NewsResponseEntity> = service.fetchArticleBySource(query, sources, page)
}
