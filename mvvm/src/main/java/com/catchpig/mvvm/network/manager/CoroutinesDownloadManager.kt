package com.catchpig.mvvm.network.manager

import android.util.ArrayMap
import com.catchpig.mvvm.entity.DownloadProgress
import com.catchpig.mvvm.listener.DownloadCallback
import com.catchpig.mvvm.listener.DownloadProgressListener
import com.catchpig.mvvm.listener.MultiDownloadCallback
import com.catchpig.mvvm.network.api.CoroutinesDownloadService
import com.catchpig.mvvm.network.download.DownloadSubscriber
import com.catchpig.mvvm.network.download.MultiDownloadSubscriber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.net.URL

/**
 * 描述:下载工具类
 * @author catchpig
 * @date 2020/11/20 10:25
 */
object CoroutinesDownloadManager : BaseDownloadManager() {
    private var downloadServiceMap: MutableMap<String, CoroutinesDownloadService> = ArrayMap()

    private fun getDowLoadService(baseUrl: String): CoroutinesDownloadService {
        return Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CoroutinesDownloadService::class.java)
    }

    /**
     * 初始化下载器接口类
     * @param baseUrl String
     * @param downloadProgressListener DownloadProgressListener
     * @return DownloadService
     */
    private fun initDownloadService(
        url: URL,
        downloadProgressListener: DownloadProgressListener
    ): CoroutinesDownloadService {
        val baseUrl = "${url.protocol}://${url.host}/"
        var downloadService = downloadServiceMap[baseUrl]
        if (downloadService == null) {
            downloadService = getDowLoadService(baseUrl)
            downloadServiceMap[baseUrl] = downloadService
        }
        downloadInterceptor.addProgressListener(url.toString(), downloadProgressListener)
        return downloadService
    }

    /**
     * 多文件下载
     * @param downloadUrls Iterable<String>
     * @param callback Function1<[@kotlin.ParameterName] MutableList<String>, Unit>
     * @param progress Function4<[@kotlin.ParameterName] Long, [@kotlin.ParameterName] Long, [@kotlin.ParameterName] Int, [@kotlin.ParameterName] Int, Unit>?
     * @return Disposable
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
     * @return Disposable
     */
    suspend fun multiDownload(
        downloadUrls: Iterable<String>,
        multiDownloadCallback: MultiDownloadCallback
    ) {
        val multiDownloadSubscriber =
            MultiDownloadSubscriber(downloadUrls.count(), multiDownloadCallback)
        flow {
            downloadUrls.forEach {
                emit(it)
            }
        }.flatMapConcat {
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
            val downloadService = initDownloadService(url, multiDownloadSubscriber)
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
     * @return Disposable
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
     * @return Disposable
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
     * @return Disposable
     */
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
            val downloadService = initDownloadService(url, downloadSubscriber)
            return@flatMapConcat httpDownload(
                downloadService,
                url.file.substring(1),
                localFilePath
            )
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
     * @return Flowable<String>
     */
    private suspend fun httpDownload(
        downloadService: CoroutinesDownloadService,
        url: String,
        localFilePath: String
    ): Flow<String> {
        return flow {
            emit(downloadService.download(url))
        }.map {
            return@map writeCache(it, localFilePath)
        }
    }
}