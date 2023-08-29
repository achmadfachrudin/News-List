package com.achmad.base.presentation.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.achmad.base.R
import com.achmad.base.core.base.Event
import com.achmad.base.theme.component.BaseButton
import com.achmad.base.theme.component.BaseSpacer
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ArticleListScreen(
    viewModelState: LiveData<MainListViewModel.State>,
    viewModelEffect: StateFlow<Event<MainListViewModel.Effect>?>,
    sendIntent: (MainListViewModel.Intent) -> Unit,
    onEffect: (effect: MainListViewModel.Effect) -> Unit,
) {
    val state by viewModelState.observeAsState(MainListViewModel.State)
    val effect = viewModelEffect.collectAsState(initial = null)
    effect.value?.getContentIfNotHandled()?.let { onEffect(it) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BaseButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.category_list),
            onClick = { sendIntent(MainListViewModel.Intent.CategoryClicked) }
        )

        BaseSpacer(16)

        BaseButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.favorite_list),
            onClick = { sendIntent(MainListViewModel.Intent.FavoriteClicked) }
        )
    }
}
