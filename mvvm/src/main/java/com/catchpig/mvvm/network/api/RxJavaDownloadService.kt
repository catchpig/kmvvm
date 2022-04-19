package com.catchpig.mvvm.network.api

import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * 下载网络接口服务
 * @author catchpig
 * @date 2020/11/20 10:25
 */
interface RxJavaDownloadService {
    /**
     * 可以断点续传
     * @param url 下载地址
     */
    @Streaming
    @GET
    fun download(@Url url: String): Flowable<ResponseBody>
}