package com.achmad.base.service

import com.achmad.common.ApiResult
import com.achmad.feature.data.entity.NewsResponseEntity
import com.skydoves.sandwich.ApiResponse
import io.mockk.coEvery
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Test
import retrofit2.Response

internal class FetchArticleListTest : AppRepositoryTest() {

    private val query = "money"
    private val source = "business"
    private val page = 1

    private val entityMock = NewsResponseEntity(
        status = "ok",
        totalResults = null,
        articles = listOf(),
        sources = listOf()
    )

    @Test
    fun `fetchArticleBySource success should return result`() {
        val result = entityMock

        coEvery {
            remote.fetchArticleBySource(query, source, page)
        } returns ApiResponse.Success(Response.success(result))

        val actual = runBlocking { repository.fetchArticlesBySource(query, source, page).last() }

        Assert.assertEquals(ApiResult.Success(result).status, actual.status)
    }

    @Test
    fun `fetchArticleBySource error should return error`() {
        val result = Response.error<NewsResponseEntity>(
            400,
            "".toResponseBody()
        )

        coEvery {
            remote.fetchArticleBySource(query, source, page)
        } returns ApiResponse.Failure.Error(result)

        val actual = runBlocking { repository.fetchArticlesBySource(query, source, page).last() }

        Assert.assertEquals(ApiResult.Error("").status, actual.status)
    }
}
