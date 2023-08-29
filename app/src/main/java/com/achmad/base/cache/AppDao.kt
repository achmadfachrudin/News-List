package com.achmad.base.cache

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.achmad.feature.data.model.Article

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: Article)

    @Delete
    suspend fun removeArticle(article: Article)

    @Query("SELECT * FROM Article WHERE url = :url_")
    suspend fun getArticle(url_: String): List<Article>

    @Query("SELECT * FROM Article")
    suspend fun getAllArticleList(): List<Article>
}
