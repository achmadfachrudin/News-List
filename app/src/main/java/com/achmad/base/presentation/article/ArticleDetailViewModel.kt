package com.achmad.base.presentation.article

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
class ArticleDetailViewModel @Inject constructor(
    private val appRepository: AppRepository,
) : BaseViewModel<
    ArticleDetailViewModel.Intent,
    ArticleDetailViewModel.State,
    ArticleDetailViewModel.Effect>(State()) {

    sealed class Intent {
        data class Viewed(val article: Article) : Intent()
        object FavoriteClicked : Intent()
    }

    data class State(
        val article: Article? = null,
        val isFavorite: Boolean = false,
    )

    sealed class Effect {
        object FinishActivity : Effect()
        data class ShowToast(val message: String) : Effect()
    }

    override fun onIntentReceived(intent: Intent) {
        when (intent) {
            is Intent.Viewed -> onViewed(intent.article)
            is Intent.FavoriteClicked -> onFavoriteClicked()
        }
    }

    private fun onViewed(article: Article) {
        viewModelScope.launch {
            setState { copy(article = article) }

            checkIsFavorite()
        }
    }

    private fun checkIsFavorite() {
        viewModelScope.launch {
            viewState.article?.let { article ->
                var isFavorite = false
                appRepository.checkIsFavorite(article).collectLatest {
                    when (it) {
                        is ApiResult.Error -> setEffect(Effect.ShowToast(it._error))
                        ApiResult.Loading -> {
                        }

                        is ApiResult.Success -> isFavorite = it._data
                    }
                }

                setState { copy(isFavorite = isFavorite) }
            }
        }
    }

    private fun onFavoriteClicked() {
        viewModelScope.launch {
            viewState.article?.let { article ->
                appRepository.updateFavorite(article).collectLatest {
                    when (it) {
                        is ApiResult.Error -> setEffect(Effect.ShowToast(it._error))
                        ApiResult.Loading -> {
                        }

                        is ApiResult.Success -> {
                            setEffect(Effect.ShowToast(it._data))
                            checkIsFavorite()
                        }
                    }
                }
            }
        }
    }
}
