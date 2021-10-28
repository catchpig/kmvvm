package com.catchpig.kmvvm.repository

import com.catchpig.kmvvm.entity.Banner
import com.catchpig.kmvvm.network.api.WanAndroidService
import com.catchpig.kmvvm.proxy.WanAndroidProxy
import com.catchpig.mvvm.network.manager.NetManager
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.Deferred

object WanAndroidRepository : WanAndroidProxy {
    private val wanAndroidService = NetManager.getService(WanAndroidService::class.java)
    override fun getBanners(): Flowable<Banner> {
        return wanAndroidService.banner().map {
            return@map it[0]
        }
    }

    override suspend fun queryBanner(): Banner {
        val banners = wanAndroidService.queryBanner()
        return banners[0]
    }
}