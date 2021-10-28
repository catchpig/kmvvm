package com.catchpig.kmvvm.proxy

import com.catchpig.kmvvm.entity.Banner
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.Deferred

interface WanAndroidProxy {
    /**
     * 获取banner图列表
     * @return MutableList<Banner>
     */
    fun getBanners(): Flowable<Banner>

    /**
     * 获取banner图列表
     * @return MutableList<Banner>
     */
    suspend fun queryBanner(): Banner
}