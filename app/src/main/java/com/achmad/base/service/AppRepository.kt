package com.achmad.base.service

import androidx.annotation.WorkerThread
import com.achmad.base.cache.AppDao
import com.achmad.common.ApiResult
import com.achmad.feature.data.mapper.mapToArticle
import com.achmad.feature.data.mapper.mapToSource
import com.achmad.feature.data.model.Article
import com.achmad.feature.data.model.Category
import com.achmad.feature.data.model.CategoryEnum
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AppRepository @Inject constructor(
    private val local: AppDao,
    private val remote: AppRemote,
) {

    @WorkerThread
    fun fetchSourcesByCategory(
        category: String,
    ) = flow {
        emit(ApiResult.Loading)

        val response = remote.fetchSourceByCategory(category)

        response.suspendOnSuccess {
            if (data.status == "ok") {
                val result = data.sources?.map { it.mapToSource() } ?: emptyList()
                emit(ApiResult.Success(result))
            } else {
                emit(ApiResult.Error("empty result"))
            }
        }.suspendOnError {
            val code = this.raw.code
            val status = this.statusCode.name
            emit(ApiResult.Error("$code $status"))
        }.suspendOnException {
            emit(ApiResult.Error(this.exception.message.orEmpty()))
        }
    }.flowOn(Dispatchers.IO)

    @WorkerThread
    fun fetchArticlesBySource(
        query: String,
        source: String,
        page: Int,
    ) = flow {
        emit(ApiResult.Loading)

        val response = remote.fetchArticleBySource(query, source, page)

        response.suspendOnSuccess {
            if (data.status == "ok") {
                val result = data.articles?.map { it.mapToArticle() } ?: emptyList<Article>()
                    .distinctBy { it.url }
                emit(ApiResult.Success(result))
            } else {
                emit(ApiResult.Error("empty result"))
            }
        }.suspendOnError {
            val code = this.raw.code
            val status = this.statusCode.name
            emit(ApiResult.Error("$code $status"))
        }.suspendOnException {
            emit(ApiResult.Error(this.exception.message.orEmpty()))
        }
    }.flowOn(Dispatchers.IO)

    @WorkerThread
    fun fetchCategories() = flow {
        val categoryEnumList = CategoryEnum.values().toList()
        val categoryList = categoryEnumList.map { Category(it.query, it.title) }

        emit(categoryList)
    }.flowOn(Dispatchers.IO)

    @WorkerThread
    fun fetchArticleFavorites() = flow {
        emit(ApiResult.Loading)

        val response = local.getAllArticleList()

        if (response.isNotEmpty()) {
            emit(ApiResult.Success(response))
        } else {
            emit(ApiResult.Error("empty result"))
        }
    }.flowOn(Dispatchers.IO)

    @WorkerThread
    fun checkIsFavorite(article: Article) = flow {
        try {
            val response = local.getArticle(article.url)
            emit(ApiResult.Success(response.isNotEmpty()))
        } catch (e: Exception) {
            emit(ApiResult.Error("error check is favorite"))
        }
    }.flowOn(Dispatchers.IO)

    @WorkerThread
    fun updateFavorite(article: Article) = flow {
        emit(ApiResult.Loading)

        try {
            val response = local.getArticle(article.url)

            if (response.isNotEmpty()) {
                local.removeArticle(article)
                emit(ApiResult.Success("removed from favorites"))
            } else {
                local.insertArticle(article)
                emit(ApiResult.Success("added to favorites"))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error("error update favorite"))
        }
    }.flowOn(Dispatchers.IO)
}
