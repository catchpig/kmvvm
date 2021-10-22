package com.catchpig.kotlin_mvvm.network.api

import com.catchpig.annotation.Service
import com.catchpig.kotlin_mvvm.network.Result
import io.reactivex.rxjava3.core.Flowable
import retrofit2.http.GET


@Service(baseUrl = "http://www.baidu.com")
interface WanAndroidService {
    @GET("banner/json")
    fun banner(): Flowable<Result<Any>>
}