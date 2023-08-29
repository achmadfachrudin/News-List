package com.achmad.base.service

import com.achmad.base.cache.AppConstant.PAGE_SIZE
import com.achmad.feature.data.entity.NewsResponseEntity
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AppService {

    @GET("top-headlines/sources")
    suspend fun fetchSourceByCategory(
        @Query("category") category: String,
    ): ApiResponse<NewsResponseEntity>

    @GET("top-headlines")
    suspend fun fetchArticleBySource(
        @Query("q") query: String,
        @Query("sources") sources: String,
        @Query("page") page: Int,
        @Query("pageSize") pagSize: Int = PAGE_SIZE,
    ): ApiResponse<NewsResponseEntity>
}
