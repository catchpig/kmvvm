package com.catchpig.kmvvm.repository

import com.catchpig.kmvvm.entity.Banner
import com.catchpig.kmvvm.network.api.WanAndroidService
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
}