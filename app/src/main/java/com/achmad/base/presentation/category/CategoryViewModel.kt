package com.achmad.base.presentation.category

import androidx.lifecycle.viewModelScope
import com.achmad.base.core.base.BaseViewModel
import com.achmad.base.service.AppRepository
import com.achmad.feature.data.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class CategoryViewModel @Inject constructor(
    val appRepository: AppRepository
) : BaseViewModel<CategoryViewModel.Intent, CategoryViewModel.State, CategoryViewModel.Effect>(
    State()
) {

    sealed class Intent {
        object Viewed : Intent()
        data class ItemClicked(val category: String) : Intent()
    }

    data class State(
        val categories: List<Category> = emptyList(),
    )

    sealed class Effect {
        object FinishActivity : Effect()

        data class OpenSourceList(val category: String) : Effect()
    }

    override fun onIntentReceived(intent: Intent) {
        when (intent) {
            Intent.Viewed -> fetchCategories()
            is Intent.ItemClicked -> {
                setEffect(Effect.OpenSourceList(intent.category))
            }
        }
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            appRepository.fetchCategories().collectLatest {
                setState {
                    copy(categories = it)
                }
            }
        }
    }
}
