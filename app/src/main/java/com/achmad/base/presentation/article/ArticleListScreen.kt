package com.achmad.base.presentation.article

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.achmad.base.R
import com.achmad.base.core.base.Event
import com.achmad.base.theme.BaseColor
import com.achmad.base.theme.Typography
import com.achmad.base.theme.component.BaseOutlinedTextField
import com.achmad.base.theme.component.BaseSpacer
import com.achmad.base.theme.component.BaseText
import com.achmad.base.theme.component.BaseToolbar
import com.achmad.base.theme.component.ErrorState
import com.achmad.base.theme.component.LoadingState
import com.achmad.feature.data.model.Article
import kotlinx.coroutines.flow.StateFlow

private const val ARTICLE_ITEM = "article_"
private const val LOAD_MORE = "load_more"

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArticleListScreen(
    viewModelState: LiveData<ArticleListViewModel.State>,
    viewModelEffect: StateFlow<Event<ArticleListViewModel.Effect>?>,
    sendIntent: (ArticleListViewModel.Intent) -> Unit,
    onEffect: (effect: ArticleListViewModel.Effect) -> Unit,
) {
    val state by viewModelState.observeAsState(ArticleListViewModel.State())
    val effect = viewModelEffect.collectAsState(initial = null)
    effect.value?.getContentIfNotHandled()?.let { onEffect(it) }

    val listState = rememberLazyListState()
    val isLoadMoreItemVisible by remember(state.displayState) {
        derivedStateOf {
            val loadMoreItem = listState.layoutInfo.visibleItemsInfo.firstOrNull {
                it.key == LOAD_MORE
            }

            loadMoreItem != null
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState
    ) {
        stickyHeader {
            BaseToolbar(
                title = stringResource(id = R.string.article_list),
                onLeftButtonClick = { onEffect(ArticleListViewModel.Effect.FinishActivity) }
            )

            BaseOutlinedTextField(
                modifier = Modifier
                    .background(BaseColor.White)
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = {
                    BaseText(text = stringResource(id = R.string.article_enter_title))
                },
                value = state.searchQuery,
                onValueChange = {
                    sendIntent(ArticleListViewModel.Intent.QueryChanged(it))
                }
            )

            BaseSpacer()
        }

        when (val display = state.displayState) {
            is ArticleListViewModel.State.DisplayState.Content -> {
                itemsIndexed(items = display.items, key = { index, item ->
                    when (item) {
                        is ItemResult.ContentItemVM -> ARTICLE_ITEM + "${index}_${item.article.url}"
                        is ItemResult.LoadMoreItemVM -> LOAD_MORE
                    }
                }) { _, item ->
                    when (item) {
                        is ItemResult.ContentItemVM -> {
                            ArticleCard(
                                model = item.article,
                                onClick = {
                                    sendIntent(
                                        ArticleListViewModel.Intent.ItemClicked(item.article)
                                    )
                                }
                            )
                        }
                        ItemResult.LoadMoreItemVM -> LoadingState()
                    }
                }
            }

            is ArticleListViewModel.State.DisplayState.Error -> item {
                ErrorState(message = display.message)
            }

            ArticleListViewModel.State.DisplayState.Loading -> item {
                LoadingState()
            }
        }
    }

    LaunchedEffect(key1 = isLoadMoreItemVisible) {
        if (isLoadMoreItemVisible) {
            sendIntent(ArticleListViewModel.Intent.LoadMore)
        }
    }
}

@Composable
fun ArticleCard(
    model: Article,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(
                width = 1.dp,
                color = BaseColor.GreyLight
            )
            .padding(16.dp)
    ) {
        BaseText(
            text = model.title,
            style = Typography.subtitle2
        )

        BaseText(
            modifier = Modifier.padding(top = 16.dp),
            text = model.description
        )

        BaseText(
            modifier = Modifier.padding(top = 16.dp),
            style = Typography.caption,
            text = model.author
        )
    }
}
