package com.catchpig.mvvm.entity

import okhttp3.Interceptor

data class ServiceParam(
    val baseUrl: String,
    val connectTimeout: Long,
    val readTimeout: Long,
    val interceptors: MutableList<Interceptor>,
    val debugInterceptors: MutableList<Interceptor>,
    val rxJava:Boolean
)
