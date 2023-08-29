package com.achmad.base.presentation.article

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Surface
import com.achmad.base.core.extension.showToastLong
import com.achmad.base.theme.BaseColor
import com.achmad.base.theme.BaseComposeTheme
import com.achmad.feature.data.model.Article
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleListActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_SOURCE = "EXTRA_SOURCE"

        fun createIntent(
            context: Context,
            source: String,
        ): Intent {
            return Intent(context, ArticleListActivity::class.java).apply {
                putExtra(EXTRA_SOURCE, source)
            }
        }
    }

    private val source by lazy { intent.getStringExtra(EXTRA_SOURCE).orEmpty() }

    private val viewModel: ArticleListViewModel by viewModels()

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
                                ArticleListViewModel.Effect.FinishActivity -> finish()
                                is ArticleListViewModel.Effect.ShowToast ->
                                    showToastLong(effect.message)
                                is ArticleListViewModel.Effect.OpenArticleDetail ->
                                    goToArticleDetail(effect.article)
                            }
                        }
                    )
                }
            }
        }

        viewModel.onIntentReceived(ArticleListViewModel.Intent.Viewed(source))
    }

    private fun goToArticleDetail(article: Article) {
        startActivity(ArticleDetailActivity.createIntent(this, article))
    }
}
