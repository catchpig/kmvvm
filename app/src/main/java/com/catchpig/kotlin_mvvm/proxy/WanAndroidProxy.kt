package com.catchpig.kotlin_mvvm.proxy

import com.catchpig.kotlin_mvvm.entity.Banner
import io.reactivex.rxjava3.core.Flowable

interface WanAndroidProxy {
    /**
     * 获取banner图列表
     * @return MutableList<Banner>
     */
    fun getBanners(): Flowable<Banner>
}