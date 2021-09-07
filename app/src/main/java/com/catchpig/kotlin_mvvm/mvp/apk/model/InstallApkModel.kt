package com.catchpig.kotlin_mvvm.mvp.apk.model

import com.catchpig.mvvm.manager.DownloadManager
import com.catchpig.mvvm.listener.DownloadCallback
import com.catchpig.mvvm.listener.MultiDownloadCallback

/**
 *
 * @author catchpig
 * @date 2020/11/20 15:51
 */
class InstallApkModel(private val downloadManager: DownloadManager) {
    fun download(url: String, downloadCallback: DownloadCallback) {
        downloadManager.download(url,downloadCallback)
    }

    fun multiDownload(urls: MutableList<String>, multiDownloadCallback: MultiDownloadCallback) {
        downloadManager.multiDownload(urls,multiDownloadCallback)
    }
}