package com.catchpig.kmvvm.article

import com.catchpig.kmvvm.entity.Article
import com.catchpig.kmvvm.repository.WanAndroidRepository
import com.catchpig.mvvm.base.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ArticleViewModel : BaseViewModel() {
    fun queryArticles(pageIndex: Int): Flow<MutableList<Article>> {
        return WanAndroidRepository.queryArticles(pageIndex)
    }
}