package com.achmad.feature.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Article(
    val title: String,
    val author: String,
    val description: String,
    @PrimaryKey val url: String,
    val publishedAt: String
) : Parcelable
