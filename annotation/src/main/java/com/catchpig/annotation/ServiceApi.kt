package com.catchpig.annotation

import okhttp3.Interceptor
import retrofit2.Converter
import kotlin.reflect.KClass

/**
 * http的service注解
 * @author catchpig
 * @date 2019/10/29 00:29
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class ServiceApi(
    /**
     * 域名或者IP
     */
    val baseUrl: String,
    /**
     * 数据转换器
     */
    val converter: KClass<out Converter<*, *>>,
    /**
     * 连接超时时间(毫秒)
     */
    val connectTimeout: Long = 5000,
    /**
     * 读取超时时间(毫秒)
     */
    val readTimeout: Long = 5000,
    /**
     * 拦截器
     */
    val interceptors: Array<KClass<out Interceptor>> = [],
    /**
     * debug模式的拦截器
     */
    val debugInterceptors: Array<KClass<out Interceptor>> = []
)

