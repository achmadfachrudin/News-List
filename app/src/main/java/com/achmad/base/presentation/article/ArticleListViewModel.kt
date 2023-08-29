package com.achmad.base.presentation.article

import androidx.lifecycle.viewModelScope
import com.achmad.base.cache.AppConstant.INITIAL_PAGE
import com.achmad.base.cache.AppConstant.PAGE_SIZE
import com.achmad.base.core.base.BaseViewModel
import com.achmad.base.presentation.article.ItemResult.LoadMoreItemVM.mapToItemVM
import com.achmad.base.service.AppRepository
import com.achmad.common.ApiResult
import com.achmad.feature.data.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class ArticleListViewModel @Inject constructor(
    private val appRepository: AppRepository,
) : BaseViewModel<
    ArticleListViewModel.Intent,
    ArticleListViewModel.State,
    ArticleListViewModel.Effect>(State()) {

    sealed class Intent {
        data class Viewed(val source: String) : Intent()
        object LoadMore : Intent()
        data class QueryChanged(val query: String) : Intent()
        data class ItemClicked(val article: Article) : Intent()
    }

    data class State(
        val source: String = "",
        val page: Int = INITIAL_PAGE,
        val displayState: DisplayState = DisplayState.Loading,
        val searchQuery: String = "",
    ) {
        sealed class DisplayState {
            data class Error(val message: String) : DisplayState()
            object Loading : DisplayState()
            data class Content(val items: List<ItemResult>) : DisplayState()
        }
    }

    sealed class Effect {
        object FinishActivity : Effect()
        data class ShowToast(val message: String) : Effect()
        data class OpenArticleDetail(val article: Article) : Effect()
    }

    private var searchJob: Job? = null

    override fun onIntentReceived(intent: Intent) {
        when (intent) {
            is Intent.ItemClicked -> setEffect(Effect.OpenArticleDetail(intent.article))
            Intent.LoadMore -> loadMore()
            is Intent.Viewed -> {
                setState { copy(source = intent.source) }

                fetchArticles()
            }

            is Intent.QueryChanged -> onQueryChanged(intent.query)
        }
    }

    private fun onQueryChanged(newQuery: String) {
        setState { copy(searchQuery = newQuery, page = INITIAL_PAGE) }

        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            delay(300)
            fetchArticles()
        }
    }

    private fun fetchArticles() {
        viewModelScope.launch {
            appRepository.fetchArticlesBySource(
                page = viewState.page,
                query = viewState.searchQuery,
                source = viewState.source
            ).collectLatest {
                when (it) {
                    is ApiResult.Error -> setState {
                        copy(displayState = State.DisplayState.Error(it._error))
                    }

                    ApiResult.Loading -> setState {
                        copy(displayState = State.DisplayState.Loading)
                    }

                    is ApiResult.Success -> {
                        val newItems = mutableListOf<ItemResult>()
                        newItems.addAll(it._data.map { article -> article.mapToItemVM() })

                        if (it._data.isNotEmpty() && it._data.size == PAGE_SIZE) {
                            newItems.add(ItemResult.LoadMoreItemVM)
                        }

                        setState {
                            copy(displayState = State.DisplayState.Content(newItems))
                        }
                    }
                }
            }
        }
    }

    private fun loadMore() {
        viewModelScope.launch {
            val page = viewState.page + 1

            setState { copy(page = page) }

            appRepository.fetchArticlesBySource(
                page = viewState.page,
                query = viewState.searchQuery,
                source = viewState.source
            ).collectLatest {
                when (it) {
                    is ApiResult.Error -> setEffect(Effect.ShowToast(it._error))

                    ApiResult.Loading -> {
                    }

                    is ApiResult.Success -> {
                        if (viewState.displayState is State.DisplayState.Content) {
                            val newItems = mutableListOf<ItemResult>()

                            val oldItems = (viewState.displayState as State.DisplayState.Content)
                                .items
                                .filterIsInstance<ItemResult.ContentItemVM>()

                            newItems.addAll(oldItems)
                            newItems.addAll(it._data.map { article -> article.mapToItemVM() })

                            if (it._data.isNotEmpty() && it._data.size == PAGE_SIZE) {
                                newItems.add(ItemResult.LoadMoreItemVM)
                            }

                            setState {
                                copy(displayState = State.DisplayState.Content(newItems))
                            }
                        }
                    }
                }
            }
        }
    }
}
