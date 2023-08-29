package com.achmad.base.presentation.source

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import com.achmad.base.theme.BaseColor
import com.achmad.base.theme.component.BaseText
import com.achmad.base.theme.component.BaseToolbar
import com.achmad.base.theme.component.ErrorState
import com.achmad.base.theme.component.LoadingState
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SourceListScreen(
    viewModelState: LiveData<SourceListViewModel.State>,
    viewModelEffect: StateFlow<Event<SourceListViewModel.Effect>?>,
    sendIntent: (SourceListViewModel.Intent) -> Unit,
    onEffect: (effect: SourceListViewModel.Effect) -> Unit,
) {
    val state by viewModelState.observeAsState(SourceListViewModel.State())
    val effect = viewModelEffect.collectAsState(initial = null)
    effect.value?.getContentIfNotHandled()?.let { onEffect(it) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        BaseToolbar(
            title = stringResource(id = R.string.source_list),
            onLeftButtonClick = { onEffect(SourceListViewModel.Effect.FinishActivity) }
        )

        when (val display = state.displayState) {
            is SourceListViewModel.State.DisplayState.Content -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(display.sources.size) {
                        val model = display.sources[it]
                        SourceCard(
                            title = model.name,
                            onClick = {
                                sendIntent(SourceListViewModel.Intent.ItemClicked(model.id))
                            }
                        )
                    }
                }
            }

            is SourceListViewModel.State.DisplayState.Error -> ErrorState(message = display.message)

            SourceListViewModel.State.DisplayState.Loading -> LoadingState()
        }
    }
}

@Composable
private fun SourceCard(
    title: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(
                width = 1.dp,
                color = BaseColor.GreyLight
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BaseText(
            text = title
        )
    }
}
