package com.catchpig.kotlin_mvvm.network.api

import com.catchpig.annotation.ServiceApi
import com.catchpig.kotlin_mvvm.entity.Banner
import com.catchpig.kotlin_mvvm.network.WanAndroidConverterFactory
import io.reactivex.rxjava3.core.Flowable
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.http.GET


@ServiceApi(baseUrl = "https://www.wanandroid.com/", factory = WanAndroidConverterFactory::class,interceptors = [HttpLoggingInterceptor::class])
interface WanAndroidService {
    @GET("banner/json")
    fun banner(): Flowable<List<Banner>>
}