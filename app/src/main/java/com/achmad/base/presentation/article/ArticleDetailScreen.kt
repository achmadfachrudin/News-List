package com.achmad.base.presentation.article

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.LiveData
import com.achmad.base.R
import com.achmad.base.core.base.Event
import com.achmad.base.theme.component.BaseSpacer
import com.achmad.base.theme.component.BaseToolbar
import com.achmad.base.theme.component.ComposeWebView
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ArticleDetailScreen(
    viewModelState: LiveData<ArticleDetailViewModel.State>,
    viewModelEffect: StateFlow<Event<ArticleDetailViewModel.Effect>?>,
    sendIntent: (ArticleDetailViewModel.Intent) -> Unit,
    onEffect: (effect: ArticleDetailViewModel.Effect) -> Unit,
) {
    val state by viewModelState.observeAsState(ArticleDetailViewModel.State())
    val effect = viewModelEffect.collectAsState(initial = null)
    effect.value?.getContentIfNotHandled()?.let { onEffect(it) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val favoriteIcon = if (state.isFavorite) {
            painterResource(id = R.drawable.ic_favorite_fill)
        } else {
            painterResource(id = R.drawable.ic_favorite_border)
        }

        BaseToolbar(
            title = stringResource(id = R.string.article_detail),
            onLeftButtonClick = { onEffect(ArticleDetailViewModel.Effect.FinishActivity) },
            showRightButton = true,
            rightButtonImage = favoriteIcon,
            onRightButtonClick = { sendIntent(ArticleDetailViewModel.Intent.FavoriteClicked) }
        )

        BaseSpacer()

        state.article?.let { ComposeWebView(it.url) }
    }
}
