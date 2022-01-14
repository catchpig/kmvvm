package com.catchpig.kmvvm.apk.viewmodel

import androidx.lifecycle.MutableLiveData
import com.catchpig.mvvm.base.viewmodel.BaseViewModel
import com.catchpig.mvvm.entity.DownloadProgress
import com.catchpig.mvvm.network.manager.DownloadManager

/**
 *
 * @author catchpig
 * @date 2020/11/20 15:51
 */
class InstallApkViewModel : BaseViewModel() {
    val progressLiveData = MutableLiveData<DownloadProgress>()
    val progressLiveData1 = MutableLiveData<DownloadProgress>()


    fun download() {
        val downloadUrlpng = "http://a.xzfile.com/apk4/apkwjql_v1.1_downcc.com.apk"
        val downloadUrlpng1 = "http://a.xzfile.com/apk4/apkpure_v3.17.32_downcc.com.apk"
        val downloadUrl = "https://wanandroid.com/blogimgs/2d120094-e1ee-47fb-a155-6eb4ca49d01f.apk"
        DownloadManager.download(downloadUrlpng, {

        }, { downloadProgress ->
            progressLiveData.value = downloadProgress
        })

        DownloadManager.multiDownload(listOf(downloadUrl, downloadUrlpng1), {

        }, { downloadProgress ->
            progressLiveData1.value = downloadProgress
        })
    }
}