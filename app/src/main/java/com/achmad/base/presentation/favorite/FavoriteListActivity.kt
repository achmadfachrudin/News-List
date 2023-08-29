package com.achmad.base.presentation.favorite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Surface
import com.achmad.base.core.extension.showToastLong
import com.achmad.base.presentation.article.ArticleDetailActivity
import com.achmad.base.theme.BaseColor
import com.achmad.base.theme.BaseComposeTheme
import com.achmad.feature.data.model.Article
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteListActivity : AppCompatActivity() {

    companion object {

        fun createIntent(
            context: Context,
        ): Intent {
            return Intent(context, FavoriteListActivity::class.java).apply {
            }
        }
    }

    private val viewModel: FavoriteListViewModel by viewModels()

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
                                FavoriteListViewModel.Effect.FinishActivity -> finish()
                                is FavoriteListViewModel.Effect.ShowToast ->
                                    showToastLong(effect.message)
                                is FavoriteListViewModel.Effect.OpenArticleDetail ->
                                    goToArticleDetail(effect.article)
                            }
                        }
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.onIntentReceived(FavoriteListViewModel.Intent.Viewed)
    }

    private fun goToArticleDetail(article: Article) {
        startActivity(ArticleDetailActivity.createIntent(this, article))
    }
}
