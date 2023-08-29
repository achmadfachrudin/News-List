package com.achmad.base.presentation.category

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Surface
import com.achmad.base.presentation.source.SourceListActivity
import com.achmad.base.theme.BaseColor
import com.achmad.base.theme.BaseComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryActivity : AppCompatActivity() {

    companion object {

        fun createIntent(
            context: Context,
        ): Intent {
            return Intent(context, CategoryActivity::class.java).apply {
            }
        }
    }

    private val viewModel: CategoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BaseComposeTheme {
                Surface(color = BaseColor.White) {
                    CategoryScreen(
                        viewModelState = viewModel.state,
                        viewModelEffect = viewModel.effect,
                        sendIntent = { viewModel.onIntentReceived(it) },
                        onEffect = { effect ->
                            when (effect) {
                                is CategoryViewModel.Effect.OpenSourceList -> {
                                    goToSourceList(effect.category)
                                }

                                CategoryViewModel.Effect.FinishActivity -> finish()
                            }
                        }
                    )
                }
            }
        }

        viewModel.onIntentReceived(CategoryViewModel.Intent.Viewed)
    }

    private fun goToSourceList(category: String) {
        startActivity(SourceListActivity.createIntent(this, category))
    }
}
