package com.catchpig.kmvvm.network.api

import com.catchpig.annotation.ServiceApi
import com.catchpig.kmvvm.network.ResponseBodyConverter

@ServiceApi(
    baseUrl = "https://www.wanandroid.com/ad/",
    converter = ResponseBodyConverter::class,
)
interface OtherService {
}