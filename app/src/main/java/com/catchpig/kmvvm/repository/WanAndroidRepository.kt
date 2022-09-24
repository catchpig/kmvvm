package com.catchpig.kmvvm.repository

import com.catchpig.kmvvm.entity.Article
import com.catchpig.kmvvm.entity.Banner
import com.catchpig.kmvvm.network.api.OtherService
import com.catchpig.kmvvm.network.api.WanAndroidService
import com.catchpig.mvvm.apt.KotlinMvvmCompiler
import com.catchpig.mvvm.network.manager.NetManager
import com.catchpig.utils.ext.md5
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

object WanAndroidRepository {
    private val wanAndroidService = NetManager.instance.getService(WanAndroidService::class.java)
    private val otherService = NetManager.instance.getService(OtherService::class.java)
    fun getBanners(): Flow<MutableList<Banner>> {
        return flow {
            val result = otherService.queryBanner()
            wanAndroidService.login("zhuazhu","123456")
            emit(wanAndroidService.queryBanner())
        }
    }

    fun queryBanner(): Flow<Banner> {
        return getBanners().map {
            return@map it[0]
        }
    }

    fun queryArticles(pageIndex: Int): Flow<MutableList<Article>> {
        val pageSize = KotlinMvvmCompiler.globalConfig().getPageSize() + 2
        return flow {
            emit(wanAndroidService.queryArticles(pageIndex, pageSize))
        }.map {
            return@map it.datas
        }
    }
}