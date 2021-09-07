package com.catchpig.mvvm.di

import com.catchpig.mvvm.config.Config
import com.catchpig.mvvm.gson.DateJsonDeserializer
import com.catchpig.mvvm.interceptor.DownloadInterceptor
import com.catchpig.mvvm.manager.DownloadManager
import com.catchpig.mvvm.network.download.DownloadService
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit


val appModule = module {
    single {
        DateJsonDeserializer()
    }
    single {
        GsonBuilder()
                .setDateFormat(Config.DATE_FORMAT)
                .registerTypeAdapter(Date::class.java,get<DateJsonDeserializer>())
                .create()
    }

    single {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        httpLoggingInterceptor
    } bind Interceptor::class
}

/**
 * 下载相关类的初始化管理
 */
const val NAMED_DOWNLOAD = "download"
val downloadModule = module {
    single(named(NAMED_DOWNLOAD)) {
        DownloadInterceptor()
    }

    single(named(NAMED_DOWNLOAD)) {
        OkHttpClient
                .Builder()
                /**
                 * 连接超时时间5秒
                 */
                .connectTimeout(5, TimeUnit.SECONDS)
                /**
                 * 读取数据超时时间10分钟
                 */
                .readTimeout(10,TimeUnit.MINUTES)
                .addInterceptor(get<Interceptor>())
                .addInterceptor(get<DownloadInterceptor>(named(NAMED_DOWNLOAD)))
                .build()
    }

    factory(named(NAMED_DOWNLOAD)) { (baseUrl:String)->
        Retrofit
                .Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(get(named(NAMED_DOWNLOAD)))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DownloadService::class.java)
    }

    single {
        DownloadManager()
    }
}