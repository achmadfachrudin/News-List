package com.achmad.base.presentation.favorite

import androidx.lifecycle.viewModelScope
import com.achmad.base.core.base.BaseViewModel
import com.achmad.base.service.AppRepository
import com.achmad.common.ApiResult
import com.achmad.feature.data.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class FavoriteListViewModel @Inject constructor(
    private val appRepository: AppRepository,
) : BaseViewModel<
    FavoriteListViewModel.Intent,
    FavoriteListViewModel.State,
    FavoriteListViewModel.Effect>(State()) {

    sealed class Intent {
        object Viewed : Intent()
        data class ItemClicked(val article: Article) : Intent()
    }

    data class State(
        val displayState: DisplayState = DisplayState.Loading,
    ) {
        sealed class DisplayState {
            data class Error(val message: String) : DisplayState()
            object Loading : DisplayState()
            data class Content(val items: List<Article>) : DisplayState()
        }
    }

    sealed class Effect {
        object FinishActivity : Effect()
        data class ShowToast(val message: String) : Effect()
        data class OpenArticleDetail(val article: Article) : Effect()
    }

    override fun onIntentReceived(intent: Intent) {
        when (intent) {
            is Intent.ItemClicked -> setEffect(Effect.OpenArticleDetail(intent.article))
            is Intent.Viewed -> fetchArticleFavorites()
        }
    }

    private fun fetchArticleFavorites() {
        viewModelScope.launch {
            appRepository.fetchArticleFavorites().collectLatest {
                when (it) {
                    is ApiResult.Error -> setState {
                        copy(displayState = State.DisplayState.Error(it._error))
                    }

                    ApiResult.Loading -> setState {
                        copy(displayState = State.DisplayState.Loading)
                    }

                    is ApiResult.Success -> {
                        setState {
                            copy(displayState = State.DisplayState.Content(it._data))
                        }
                    }
                }
            }
        }
    }
}
