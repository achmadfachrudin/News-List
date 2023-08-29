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

internal class FetchSourceListTest : AppRepositoryTest() {

    private val category = "business"

    private val entityMock = NewsResponseEntity(
        status = "ok",
        totalResults = null,
        articles = listOf(),
        sources = listOf()
    )

    @Test
    fun `fetchSourceByCategory success should return result`() {
        val result = entityMock

        coEvery {
            remote.fetchSourceByCategory(category)
        } returns ApiResponse.Success(Response.success(result))

        val actual = runBlocking { repository.fetchSourcesByCategory(category).last() }

        Assert.assertEquals(ApiResult.Success(result).status, actual.status)
    }

    @Test
    fun `fetchSourceByCategory error should return error`() {
        val result = Response.error<NewsResponseEntity>(
            400,
            "".toResponseBody()
        )

        coEvery {
            remote.fetchSourceByCategory(category)
        } returns ApiResponse.Failure.Error(result)

        val actual = runBlocking { repository.fetchSourcesByCategory(category).last() }

        Assert.assertEquals(ApiResult.Error("").status, actual.status)
    }
}
