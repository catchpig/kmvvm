package com.catchpig.kmvvm.network.api

import com.catchpig.annotation.ServiceApi
import com.catchpig.kmvvm.network.WanAndroidConverterFactory

@ServiceApi(
    baseUrl = "https://www.wanandroid.com/ad/",
    factory = WanAndroidConverterFactory::class
)
interface OtherService {
}