package com.achmad.base.presentation.favorite

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.LiveData
import com.achmad.base.R
import com.achmad.base.core.base.Event
import com.achmad.base.presentation.article.ArticleCard
import com.achmad.base.theme.component.BaseSpacer
import com.achmad.base.theme.component.BaseToolbar
import com.achmad.base.theme.component.ErrorState
import com.achmad.base.theme.component.LoadingState
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArticleListScreen(
    viewModelState: LiveData<FavoriteListViewModel.State>,
    viewModelEffect: StateFlow<Event<FavoriteListViewModel.Effect>?>,
    sendIntent: (FavoriteListViewModel.Intent) -> Unit,
    onEffect: (effect: FavoriteListViewModel.Effect) -> Unit,
) {
    val state by viewModelState.observeAsState(FavoriteListViewModel.State())
    val effect = viewModelEffect.collectAsState(initial = null)
    effect.value?.getContentIfNotHandled()?.let { onEffect(it) }

    val listState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState
    ) {
        stickyHeader {
            BaseToolbar(
                title = stringResource(id = R.string.favorite_list),
                onLeftButtonClick = { onEffect(FavoriteListViewModel.Effect.FinishActivity) }
            )

            BaseSpacer()
        }

        when (val display = state.displayState) {
            is FavoriteListViewModel.State.DisplayState.Content -> {
                items(display.items) {
                    ArticleCard(
                        model = it,
                        onClick = {
                            sendIntent(
                                FavoriteListViewModel.Intent.ItemClicked(it)
                            )
                        }
                    )
                }
            }

            is FavoriteListViewModel.State.DisplayState.Error -> item {
                ErrorState(message = display.message)
            }

            FavoriteListViewModel.State.DisplayState.Loading -> item {
                LoadingState()
            }
        }
    }
}
