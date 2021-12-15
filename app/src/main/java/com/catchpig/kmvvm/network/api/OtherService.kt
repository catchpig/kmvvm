package com.catchpig.kmvvm.network.api

import com.catchpig.annotation.ServiceApi
import com.catchpig.kmvvm.network.Result
import com.catchpig.mvvm.network.converter.SerializationResponseBodyConverter
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import retrofit2.http.GET

@ServiceApi(
    baseUrl = "https://www.wanandroid.com/",
    responseConverter = SerializationResponseBodyConverter::class,
    debugInterceptors = [OkHttpProfilerInterceptor::class]
)
interface OtherService {
    @GET("banner/json")
    suspend fun queryBanner(): Result
}