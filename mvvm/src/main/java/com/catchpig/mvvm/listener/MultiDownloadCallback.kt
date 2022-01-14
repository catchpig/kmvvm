package com.catchpig.mvvm.listener

import com.catchpig.mvvm.entity.DownloadProgress

/**
 * 下载回调接口
 * @author catchpig
 * @date 2020/11/20 10:25
 */
interface MultiDownloadCallback {
    /**
     * 开始下载
     */
    fun onStart()

    /**
     * 文件下载进度
     * @param downloadProgress DownloadProgress
     */
    fun onProgress(downloadProgress: DownloadProgress)

    /**
     * 下载成功
     * @param paths 本地保存的地址集
     */
    fun onSuccess(paths: MutableList<String>)

    /**
     * 下载完成
     */
    fun onComplete()

    /**
     * 下载错误
     * @param t 错误信息
     */
    fun onError(t: Throwable)
}