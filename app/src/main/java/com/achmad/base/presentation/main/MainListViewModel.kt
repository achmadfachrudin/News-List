package com.achmad.base.presentation.main

import com.achmad.base.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainListViewModel @Inject constructor() : BaseViewModel<
    MainListViewModel.Intent,
    MainListViewModel.State,
    MainListViewModel.Effect>(State) {

    sealed class Intent {
        object CategoryClicked : Intent()
        object FavoriteClicked : Intent()
    }

    object State

    sealed class Effect {
        object FinishActivity : Effect()
        object OpenCategoryList : Effect()
        object OpenFavoriteList : Effect()
    }

    override fun onIntentReceived(intent: Intent) {
        when (intent) {
            is Intent.CategoryClicked -> setEffect(Effect.OpenCategoryList)
            is Intent.FavoriteClicked -> setEffect(Effect.OpenFavoriteList)
        }
    }
}
