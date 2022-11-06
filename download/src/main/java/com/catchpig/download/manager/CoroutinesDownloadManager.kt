package com.catchpig.download.manager

import com.catchpig.download.api.DownloadService
import com.catchpig.download.callback.DownloadCallback
import com.catchpig.download.callback.MultiDownloadCallback
import com.catchpig.download.entity.DownloadProgress
import com.catchpig.download.subscriber.DownloadSubscriber
import com.catchpig.download.subscriber.MultiDownloadSubscriber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import java.io.File
import java.net.URL

/**
 * 描述:下载工具类
 * @author catchpig
 * @date 2020/11/20 10:25
 */
class CoroutinesDownloadManager : DownloadManager() {
    companion object {
        fun getInstance(): CoroutinesDownloadManager {
            return CoroutinesDownloadManagerHolder.holder
        }
    }

    private object CoroutinesDownloadManagerHolder {
        val holder = CoroutinesDownloadManager()
    }

    /**
     * 多文件下载
     * @param downloadUrls Iterable<String>
     * @param callback Function1<[@kotlin.ParameterName] MutableList<String>, Unit>
     * @param progress Function4<[@kotlin.ParameterName] Long, [@kotlin.ParameterName] Long, [@kotlin.ParameterName] Int, [@kotlin.ParameterName] Int, Unit>?
     */
    suspend fun multiDownload(
        downloadUrls: Iterable<String>,
        callback: (paths: MutableList<String>) -> Unit,
        progress: ((downloadProgress: DownloadProgress) -> Unit)? = null
    ) {
        multiDownload(downloadUrls, object : MultiDownloadCallback {
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
     */
    @OptIn(FlowPreview::class)
    suspend fun multiDownload(
        downloadUrls: Iterable<String>,
        multiDownloadCallback: MultiDownloadCallback
    ) {
        val multiDownloadSubscriber =
            MultiDownloadSubscriber(downloadUrls.count(), multiDownloadCallback)
        downloadUrls.asFlow().flatMapConcat {
            val localFilePath = localFileName(it)
            val file = File(localFilePath)
            val url = URL(it)
            if (file.exists()) {
                val fileLength = file.length()
                val contentLength = url.openConnection().contentLength
                if (fileLength == contentLength.toLong()) {
                    multiDownloadSubscriber.update(fileLength, fileLength, true)
                    return@flatMapConcat flowOf(localFilePath)
                }
            }
            val downloadService = initDownloadService(url, multiDownloadSubscriber, false)
            return@flatMapConcat httpDownload(
                downloadService,
                url.file.substring(1),
                localFilePath
            )
        }.flowOn(Dispatchers.IO).onStart {
            multiDownloadSubscriber.onStart()
        }.onCompletion {
            multiDownloadSubscriber.onComplete()
        }.catch { t: Throwable ->
            multiDownloadSubscriber.onError(t)
        }.collect {
            multiDownloadSubscriber.onNext(it)
        }
    }

    /**
     * 下载返回File
     * @param downloadUrl String
     * @param callback Function1<[@kotlin.ParameterName] File, Unit>
     * @param progress Function2<[@kotlin.ParameterName] Long, [@kotlin.ParameterName] Long, Unit>
     */
    suspend fun downloadFile(
        downloadUrl: String,
        callback: (file: File) -> Unit,
        progress: ((downloadProgress: DownloadProgress) -> Unit)? = null
    ) {
        download(downloadUrl, object : DownloadCallback {
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
     */
    suspend fun download(
        downloadUrl: String,
        callback: (path: String) -> Unit,
        progress: ((downloadProgress: DownloadProgress) -> Unit)? = null
    ) {
        download(downloadUrl, object : DownloadCallback {
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
     */
    @OptIn(FlowPreview::class)
    suspend fun download(
        downloadUrl: String,
        downloadCallback: DownloadCallback
    ) {
        val downloadSubscriber = DownloadSubscriber(downloadCallback)
        flowOf(downloadUrl).flatMapConcat {
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
                    return@flatMapConcat flowOf(localFilePath).flowOn(Dispatchers.IO)
                }
            }
            val downloadService = initDownloadService(url, downloadSubscriber, false)
            return@flatMapConcat httpDownload(downloadService, url.file.substring(1), localFilePath)
        }.flowOn(Dispatchers.IO).onStart {
            downloadSubscriber.onStart()
        }.onCompletion {
            downloadSubscriber.onComplete()
        }.catch { t: Throwable ->
            downloadSubscriber.onError(t)
        }.collect {
            downloadSubscriber.onNext(it)
        }
    }

    /**
     * http下载
     * @param downloadService DownloadService
     * @param url String
     * @param localFilePath String
     * @return Flow<String>
     */
    private suspend fun httpDownload(
        downloadService: DownloadService,
        url: String,
        localFilePath: String
    ): Flow<String> {
        return flow {
            emit(downloadService.coroutinesDownload(url))
        }.map {
            return@map writeCache(it, localFilePath)
        }
    }
}