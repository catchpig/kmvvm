package com.catchpig.kotlin_mvvm.repository

import com.catchpig.kotlin_mvvm.entity.Banner
import com.catchpig.kotlin_mvvm.network.api.WanAndroidService
import com.catchpig.kotlin_mvvm.proxy.WanAndroidProxy
import com.catchpig.mvvm.network.manager.NetManager
import io.reactivex.rxjava3.core.Flowable

object WanAndroidRepository : WanAndroidProxy {
    private val wanAndroidService = NetManager.getService(WanAndroidService::class.java)
    override fun getBanners(): Flowable<Banner> {
        return wanAndroidService.banner().map {
            return@map it[0]
        }
    }
}