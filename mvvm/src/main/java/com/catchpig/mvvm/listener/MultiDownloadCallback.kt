package com.catchpig.mvvm.listener

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
     * @param readLength Long 当前正在下载文件的已下载长度
     * @param countLength Long 当前正在下载文件的总长度
     * @param completeCount Int 已完成文件下载的个数
     * @param TotalCount Int 总文件个数
     */
    fun onProgress(readLength: Long, countLength: Long, completeCount: Int, TotalCount: Int)

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