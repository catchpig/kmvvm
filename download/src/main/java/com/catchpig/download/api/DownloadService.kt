package com.catchpig.download.api

import io.reactivex.rxjava3.core.Flowable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * 下载网络接口服务
 * @author catchpig
 * @date 2020/11/20 10:25
 */
interface DownloadService {
    /**
     * 可以断点续传(Rxjava)
     * @param url 下载地址
     */
    @Streaming
    @GET
    fun rxJavaDownload(@Url url: String): Flowable<ResponseBody>

    /**
     * 可以断点续传(协程)
     * @param url 下载地址
     */
    @Streaming
    @GET
    suspend fun coroutinesDownload(@Url url: String): ResponseBody
}