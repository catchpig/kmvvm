package com.catchpig.kotlin_mvvm.mvvm.main.model

import com.catchpig.kotlin_mvvm.network.Result
import com.catchpig.kotlin_mvvm.network.api.WanAndroidService
import io.reactivex.rxjava3.core.Flowable

class MainModel(private val wanAndroidService: WanAndroidService) {
    fun banner(): Flowable<Result<Any>> {
        return wanAndroidService.banner()
    }
}