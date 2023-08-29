package com.achmad.base.presentation.article

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Surface
import com.achmad.base.core.extension.showToast
import com.achmad.base.theme.BaseColor
import com.achmad.base.theme.BaseComposeTheme
import com.achmad.feature.data.model.Article
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_URL = "EXTRA_URL"

        fun createIntent(
            context: Context,
            article: Article,
        ): Intent {
            return Intent(context, ArticleDetailActivity::class.java).apply {
                putExtra(EXTRA_URL, article)
            }
        }
    }

    private val article by lazy { intent.getParcelableExtra<Article>(EXTRA_URL) }

    private val viewModel: ArticleDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BaseComposeTheme {
                Surface(color = BaseColor.White) {
                    ArticleDetailScreen(
                        viewModelState = viewModel.state,
                        viewModelEffect = viewModel.effect,
                        sendIntent = { viewModel.onIntentReceived(it) },
                        onEffect = { effect ->
                            when (effect) {
                                ArticleDetailViewModel.Effect.FinishActivity -> finish()
                                is ArticleDetailViewModel.Effect.ShowToast -> showToast(
                                    effect.message
                                )
                            }
                        }
                    )
                }
            }
        }

        article?.let {
            viewModel.onIntentReceived(ArticleDetailViewModel.Intent.Viewed(it))
        }
    }
}
