package com.achmad.base.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Surface
import com.achmad.base.presentation.category.CategoryActivity
import com.achmad.base.presentation.favorite.FavoriteListActivity
import com.achmad.base.theme.BaseColor
import com.achmad.base.theme.BaseComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {

        fun createIntent(
            context: Context,
        ): Intent {
            return Intent(context, MainActivity::class.java).apply {
            }
        }
    }

    private val viewModel: MainListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BaseComposeTheme {
                Surface(color = BaseColor.White) {
                    ArticleListScreen(
                        viewModelState = viewModel.state,
                        viewModelEffect = viewModel.effect,
                        sendIntent = { viewModel.onIntentReceived(it) },
                        onEffect = { effect ->
                            when (effect) {
                                MainListViewModel.Effect.FinishActivity -> finish()
                                is MainListViewModel.Effect.OpenCategoryList -> goToCategories()
                                is MainListViewModel.Effect.OpenFavoriteList -> goToFavorites()
                            }
                        }
                    )
                }
            }
        }
    }

    private fun goToCategories() {
        startActivity(CategoryActivity.createIntent(this))
    }

    private fun goToFavorites() {
        startActivity(FavoriteListActivity.createIntent(this))
    }
}
