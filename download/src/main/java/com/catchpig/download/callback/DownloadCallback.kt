package com.catchpig.download.callback

import com.catchpig.download.entity.DownloadProgress

/**
 * 下载回调接口
 * @author catchpig
 * @date 2020/11/20 10:25
 */
interface DownloadCallback {
    /**
     * 开始下载
     */
    fun onStart()

    /**
     * 下载成功
     * @param path 本地保存的地址
     */
    fun onSuccess(path: String)

    /**
     * 下载完成
     */
    fun onComplete()

    /**
     * 下载进度
     * @param downloadProgress DownloadProgress
     */
    fun onProgress(downloadProgress: DownloadProgress)

    /**
     * 下载错误
     * @param t 错误信息
     */
    fun onError(t: Throwable)
}