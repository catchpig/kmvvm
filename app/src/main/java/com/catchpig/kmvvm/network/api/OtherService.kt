package com.catchpig.kmvvm.network.api

import com.catchpig.annotation.ServiceApi
import com.catchpig.kmvvm.network.ResponseBodyConverter
import com.catchpig.mvvm.network.converter.GsonResponseBodyConverter

@ServiceApi(
    baseUrl = "https://www.wanandroid.com/ad/",
    responseConverter = ResponseBodyConverter::class
)
interface OtherService {
}