package com.achmad.feature.data.mapper

import com.achmad.feature.data.entity.ArticleEntity
import com.achmad.feature.data.entity.NewsResponseEntity
import com.achmad.feature.data.entity.SourceEntity
import com.achmad.feature.data.model.Article
import com.achmad.feature.data.model.NewsResponse
import com.achmad.feature.data.model.Source
import org.junit.Assert
import org.junit.Test

internal class MapperKtTest {

    @Test
    fun mapToNewsResponse() {
        val entity = NewsResponseEntity(
            status = "ok",
            totalResults = null,
            articles = listOf(),
            sources = listOf()
        )

        val actual = entity.mapToNewsResponse()

        val expected = NewsResponse(
            status = "ok",
            totalResults = 0,
            articles = listOf(),
            sources = listOf()
        )

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun mapToSource() {
        val entity = SourceEntity(
            id = "123",
            name = "name",
            description = null,
            url = null,
            category = null,
            language = null,
            country = null
        )

        val actual = entity.mapToSource()

        val expected = Source(
            id = "123",
            name = "name",
            description = "",
            url = "",
            category = "",
            language = "",
            country = ""
        )

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun mapToArticle() {
        val entity = ArticleEntity(
            title = "title",
            author = "author",
            description = "",
            url = "",
            publishedAt = ""
        )

        val actual = entity.mapToArticle()

        val expected = Article(
            title = "title",
            author = "author",
            description = "",
            url = "",
            publishedAt = ""
        )

        Assert.assertEquals(expected, actual)
    }
}
