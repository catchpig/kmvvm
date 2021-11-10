package com.catchpig.kmvvm.repository

import com.catchpig.kmvvm.entity.Article
import com.catchpig.kmvvm.entity.Banner
import com.catchpig.kmvvm.network.api.WanAndroidService
import com.catchpig.mvvm.apt.KotlinMvvmCompiler
import com.catchpig.mvvm.network.manager.NetManager

object WanAndroidRepository {
    private val wanAndroidService = NetManager.getService(WanAndroidService::class.java)
    suspend fun getBanners(): MutableList<Banner> {
        return wanAndroidService.queryBanner()
    }

    suspend fun queryBanner(): Banner {
        val banners = wanAndroidService.queryBanner()
        return banners[0]
    }

    suspend fun queryArticles(pageIndex: Int): MutableList<Article> {
        return wanAndroidService.queryArticles(
            pageIndex,
            KotlinMvvmCompiler.globalConfig().getPageSize()+2
        ).datas
    }
}