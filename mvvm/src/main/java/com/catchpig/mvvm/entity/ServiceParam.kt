package com.catchpig.mvvm.entity

import okhttp3.Interceptor
import retrofit2.Converter

data class ServiceParam(
    val baseUrl: String,
    val factory: Converter.Factory,
    val connectTimeout: Long,
    val readTimeout: Long,
    val interceptors: MutableList<Interceptor>
)
