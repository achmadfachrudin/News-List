package com.achmad.base.presentation.article

import com.achmad.feature.data.model.Article

sealed class ItemResult {
    data class ContentItemVM(
        val article: Article,
    ) : ItemResult()

    internal fun Article.mapToItemVM(): ContentItemVM {
        return ContentItemVM(
            article = this
        )
    }

    object LoadMoreItemVM : ItemResult() {
        val id: String
            get() = "load-more"
    }
}
