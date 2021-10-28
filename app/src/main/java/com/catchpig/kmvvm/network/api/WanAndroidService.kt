package com.catchpig.kmvvm.network.api

import com.catchpig.annotation.ServiceApi
import com.catchpig.kmvvm.entity.Banner
import com.catchpig.kmvvm.network.WanAndroidConverterFactory
import io.reactivex.rxjava3.core.Flowable
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.http.GET


@ServiceApi(
    baseUrl = "https://www.wanandroid.com/",
    factory = WanAndroidConverterFactory::class
)
interface WanAndroidService {
    @GET("banner/json")
    fun banner(): Flowable<List<Banner>>

    @GET("banner/json")
    suspend fun queryBanner(): List<Banner>
}