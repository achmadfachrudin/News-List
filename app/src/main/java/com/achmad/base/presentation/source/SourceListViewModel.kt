package com.achmad.base.presentation.source

import androidx.lifecycle.viewModelScope
import com.achmad.base.core.base.BaseViewModel
import com.achmad.base.service.AppRepository
import com.achmad.common.ApiResult
import com.achmad.feature.data.model.Source
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class SourceListViewModel @Inject constructor(
    private val appRepository: AppRepository,
) : BaseViewModel<
    SourceListViewModel.Intent,
    SourceListViewModel.State,
    SourceListViewModel.Effect>(
    State()
) {

    sealed class Intent {
        data class Viewed(val category: String) : Intent()
        data class ItemClicked(val source: String) : Intent()
    }

    data class State(
        val displayState: DisplayState = DisplayState.Loading,
    ) {
        sealed class DisplayState {
            data class Error(val message: String) : DisplayState()
            object Loading : DisplayState()
            data class Content(val sources: List<Source>) : DisplayState()
        }
    }

    sealed class Effect {
        object FinishActivity : Effect()
        data class OpenArticleList(val sourceId: String) : Effect()
    }

    override fun onIntentReceived(intent: Intent) {
        when (intent) {
            is Intent.Viewed -> fetchSources(intent.category)
            is Intent.ItemClicked -> setEffect(Effect.OpenArticleList(intent.source))
        }
    }

    private fun fetchSources(category: String) {
        viewModelScope.launch {
            appRepository.fetchSourcesByCategory(category).collectLatest {
                when (it) {
                    is ApiResult.Error -> setState {
                        copy(displayState = State.DisplayState.Error(it._error))
                    }

                    ApiResult.Loading -> setState {
                        copy(displayState = State.DisplayState.Loading)
                    }

                    is ApiResult.Success -> setState {
                        copy(displayState = State.DisplayState.Content(it._data))
                    }
                }
            }
        }
    }
}
