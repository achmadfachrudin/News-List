package com.achmad.base.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.achmad.feature.data.model.Article

@Database(entities = [Article::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao
}
