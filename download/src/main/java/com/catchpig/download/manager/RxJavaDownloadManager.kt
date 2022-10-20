package com.catchpig.download.manager

import com.catchpig.download.api.DownloadService
import com.catchpig.download.callback.DownloadCallback
import com.catchpig.download.callback.MultiDownloadCallback
import com.catchpig.download.entity.DownloadProgress
import com.catchpig.download.subscriber.DownloadSubscriber
import com.catchpig.download.subscriber.MultiDownloadSubscriber
import com.catchpig.utils.ext.io2main
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File
import java.net.URL

/**
 * 描述:下载工具类
 * @author catchpig
 * @date 2020/11/20 10:25
 */
class RxJavaDownloadManager : DownloadManager() {
    companion object {
        fun getInstance(): RxJavaDownloadManager {
            return RxJavaDownloadManagerHolder.holder
        }
    }

    private object RxJavaDownloadManagerHolder {
        val holder = RxJavaDownloadManager()
    }

    /**
     * 多文件下载
     * @param downloadUrls Iterable<String>
     * @param callback Function1<[@kotlin.ParameterName] MutableList<String>, Unit>
     * @param progress Function4<[@kotlin.ParameterName] Long, [@kotlin.ParameterName] Long, [@kotlin.ParameterName] Int, [@kotlin.ParameterName] Int, Unit>?
     * @return Disposable
     */
    fun multiDownload(
        downloadUrls: Iterable<String>,
        callback: (paths: MutableList<String>) -> Unit,
        progress: ((downloadProgress: DownloadProgress) -> Unit)? = null
    ): Disposable {
        return multiDownload(downloadUrls, object : MultiDownloadCallback {
            override fun onStart() {

            }

            override fun onComplete() {

            }

            override fun onError(t: Throwable) {

            }

            override fun onProgress(downloadProgress: DownloadProgress) {
                progress?.let { it(downloadProgress) }
            }

            override fun onSuccess(paths: MutableList<String>) {
                callback(paths)
            }
        })
    }

    /**
     * 多文件下载
     * @param downloadUrls Iterable<String>
     * @param multiDownloadCallback MultiDownloadCallback
     * @return Disposable
     */
    fun multiDownload(
        downloadUrls: Iterable<String>,
        multiDownloadCallback: MultiDownloadCallback
    ): Disposable {
        val multiDownloadSubscriber =
            MultiDownloadSubscriber(downloadUrls.count(), multiDownloadCallback)
        return Flowable.fromIterable(downloadUrls).concatMap {
            val localFilePath = localFileName(it)
            val file = File(localFilePath)
            val url = URL(it)
            if (file.exists()) {
                val fileLength = file.length()
                val contentLength = url.openConnection().contentLength
                if (fileLength == contentLength.toLong()) {
                    multiDownloadSubscriber.update(fileLength, fileLength, true)
                    return@concatMap Flowable.just(localFilePath)
                }
            }
            val downloadService = initDownloadService(url, multiDownloadSubscriber, true)
            return@concatMap httpDownload(downloadService, url.file.substring(1), localFilePath)
        }.io2main().subscribeWith(multiDownloadSubscriber)
    }

    /**
     * 下载返回File
     * @param downloadUrl String
     * @param callback Function1<[@kotlin.ParameterName] File, Unit>
     * @param progress Function2<[@kotlin.ParameterName] Long, [@kotlin.ParameterName] Long, Unit>
     * @return Disposable
     */
    fun downloadFile(
        downloadUrl: String,
        callback: (file: File) -> Unit,
        progress: ((downloadProgress: DownloadProgress) -> Unit)? = null
    ): Disposable {
        return download(downloadUrl, object : DownloadCallback {
            override fun onStart() {

            }

            override fun onComplete() {

            }

            override fun onProgress(downloadProgress: DownloadProgress) {
                progress?.let { it(downloadProgress) }
            }

            override fun onError(t: Throwable) {

            }

            override fun onSuccess(path: String) {
                callback(File(path))
            }
        })
    }

    /**
     * 下载返回路径地址
     * @param downloadUrl String
     * @param callback Function1<[@kotlin.ParameterName] String, Unit>
     * @param progress Function2<[@kotlin.ParameterName] Long, [@kotlin.ParameterName] Long, Unit>
     * @return Disposable
     */
    fun download(
        downloadUrl: String,
        callback: (path: String) -> Unit,
        progress: ((downloadProgress: DownloadProgress) -> Unit)? = null
    ): Disposable {
        return download(downloadUrl, object : DownloadCallback {
            override fun onStart() {

            }

            override fun onComplete() {

            }

            override fun onProgress(downloadProgress: DownloadProgress) {
                progress?.let { it(downloadProgress) }
            }

            override fun onError(t: Throwable) {

            }

            override fun onSuccess(path: String) {
                callback(path)
            }
        })
    }

    /**
     * 单文件下载
     * @param downloadUrl String 下载地址
     * @param downloadCallback DownLoadCallback 下载回调接口,回调的方法已经切到主线程
     * @return Disposable
     */
    fun download(downloadUrl: String, downloadCallback: DownloadCallback): Disposable {
        val downloadSubscriber = DownloadSubscriber(downloadCallback)
        Flowable.just(downloadUrl).flatMap {
            /**
             * 判断本地是否存在下载的文件,
             * 如果存在,文件的大小和远程文件大小一样,直接返回本地文件的路劲给回调,
             * 如果不存在,远程下载文件
             */
            val localFilePath = localFileName(it)
            val file = File(localFilePath)
            val url = URL(it)
            if (file.exists()) {
                val fileLength = file.length()
                val contentLength = url.openConnection().contentLength
                if (fileLength == contentLength.toLong()) {
                    downloadSubscriber.update(fileLength, fileLength, true)
                    return@flatMap Flowable.just(localFilePath)
                }
            }
            val downloadService = initDownloadService(url, downloadSubscriber, true)
            return@flatMap httpDownload(downloadService, url.file.substring(1), localFilePath)
        }.io2main().subscribeWith(downloadSubscriber)
        return downloadSubscriber
    }

    /**
     * http下载
     * @param downloadService DownloadService
     * @param url String
     * @param localFilePath String
     * @return Flowable<String>
     */
    private fun httpDownload(
        downloadService: DownloadService,
        url: String,
        localFilePath: String
    ): Flowable<String> {
        return downloadService.rxJavaDownload(url).subscribeOn(Schedulers.io()).map {
            return@map writeCache(it, localFilePath)
        }
    }

}