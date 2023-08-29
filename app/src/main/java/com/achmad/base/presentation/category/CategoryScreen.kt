package com.achmad.base.presentation.category

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryScreen(
    viewModelState: LiveData<CategoryViewModel.State>,
    viewModelEffect: StateFlow<Event<CategoryViewModel.Effect>?>,
    sendIntent: (CategoryViewModel.Intent) -> Unit,
    onEffect: (effect: CategoryViewModel.Effect) -> Unit,
) {
    val state by viewModelState.observeAsState(CategoryViewModel.State())
    val effect = viewModelEffect.collectAsState(initial = null)
    effect.value?.getContentIfNotHandled()?.let { onEffect(it) }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        stickyHeader {
            BaseToolbar(
                title = stringResource(id = R.string.category_list),
                onLeftButtonClick = { onEffect(CategoryViewModel.Effect.FinishActivity) }
            )
        }

        items(state.categories.size) {
            val model = state.categories[it]
            CategoryCard(
                title = model.title,
                onClick = {
                    sendIntent(CategoryViewModel.Intent.ItemClicked(model.query))
                }
            )
        }
    }
}

@Composable
private fun CategoryCard(
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
