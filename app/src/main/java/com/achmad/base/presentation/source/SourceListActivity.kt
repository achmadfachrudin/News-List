package com.achmad.base.presentation.source

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Surface
import com.achmad.base.presentation.article.ArticleListActivity
import com.achmad.base.theme.BaseColor
import com.achmad.base.theme.BaseComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SourceListActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_CATEGORY = "EXTRA_CATEGORY"

        fun createIntent(
            context: Context,
            category: String,
        ): Intent {
            return Intent(context, SourceListActivity::class.java).apply {
                putExtra(EXTRA_CATEGORY, category)
            }
        }
    }

    private val category by lazy { intent.getStringExtra(EXTRA_CATEGORY).orEmpty() }

    private val viewModel: SourceListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BaseComposeTheme {
                Surface(color = BaseColor.White) {
                    SourceListScreen(
                        viewModelState = viewModel.state,
                        viewModelEffect = viewModel.effect,
                        sendIntent = { viewModel.onIntentReceived(it) },
                        onEffect = { effect ->
                            when (effect) {
                                SourceListViewModel.Effect.FinishActivity -> finish()
                                is SourceListViewModel.Effect.OpenArticleList ->
                                    goToArticleList(effect.sourceId)
                            }
                        }
                    )
                }
            }
        }

        viewModel.onIntentReceived(SourceListViewModel.Intent.Viewed(category))
    }

    private fun goToArticleList(source: String) {
        startActivity(ArticleListActivity.createIntent(this, source))
    }
}
