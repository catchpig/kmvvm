package com.catchpig.mvvm.listener
/**
 * 下载进度回调接口
  * @author catchpig
 * @date 2020/11/20 10:25
 */
interface DownloadProgressListener {
    /**
     * 下载进度
     * @param read 当前读取数据值
     * @param count 总数据值
     * @param done 是否完成
     */
    fun update(read: Long, count: Long, done: Boolean)
}