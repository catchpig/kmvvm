package com.catchpig.mvvm.network.manager

import com.catchpig.mvvm.apt.KotlinMvvmCompiler
import com.catchpig.mvvm.entity.ServiceParam
import com.catchpig.mvvm.network.factory.SerializationCoverterFactory
import com.catchpig.utils.ext.logd
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import java.util.concurrent.TimeUnit

object NetManager {
    private const val TAG = "NetManager"
    private var debug = false
    private val serviceMap = hashMapOf<String, Any>()

    fun setDebug(debug: Boolean) {
        this.debug = debug
    }

    fun <S> getService(serviceClass: Class<S>): S {
        val className = serviceClass.name
        val service = serviceMap[className]
        return if (service == null) {
            val serviceParam = KotlinMvvmCompiler.getServiceParam(className)
            var builder = Retrofit
                .Builder()
                .baseUrl(serviceParam.baseUrl)
                .client(getClient(serviceParam))
                .addConverterFactory(SerializationCoverterFactory.create(className))
            if (serviceParam.rxJava) {
                builder = builder.addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            }
            val newService = builder.build().create(serviceClass)
            serviceMap[className] = newService!!
            newService
        } else {
            service as S
        }
    }

    private fun getClient(serviceParam: ServiceParam): OkHttpClient {
        var builder = OkHttpClient
            .Builder()
            /**
             * 连接超时时间
             */
            .connectTimeout(serviceParam.connectTimeout, TimeUnit.MILLISECONDS)
            /**
             * 读取数据超时时间
             */
            .readTimeout(serviceParam.readTimeout, TimeUnit.MILLISECONDS)
        if (debug) {
            serviceParam.debugInterceptors.forEach {
                builder = builder.addInterceptor(it)
            }
            val loggingInterceptor = HttpLoggingInterceptor {
                it.logd(TAG)
            }
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            builder = builder.addInterceptor(loggingInterceptor)
        }
        serviceParam.interceptors.forEach {
            builder = builder.addInterceptor(it)
        }
        return builder.build()
    }
}