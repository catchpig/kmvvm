package com.catchpig.kmvvm.apk.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.catchpig.mvvm.base.viewmodel.BaseViewModel
import com.catchpig.mvvm.entity.DownloadProgress
import com.catchpig.mvvm.network.manager.CoroutinesDownloadManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        val downloadUrlpng1 =
            "https://imtt.dd.qq.com/16891/myapp/channel_102497291_1165285_b0e7c10e19b54b31dfaf1fd4c0150e44.apk?hsr=5848"
        val downloadUrl = "https://wanandroid.com/blogimgs/2d120094-e1ee-47fb-a155-6eb4ca49d01f.apk"
        viewModelScope.launch(Dispatchers.Main) {
//            RxJavaDownloadManager.download(downloadUrlpng, {
//
//            }, { downloadProgress ->
//                progressLiveData.value = downloadProgress
//            })
//
//            RxJavaDownloadManager.multiDownload(listOf(downloadUrl, downloadUrlpng1), {
//
//            }, { downloadProgress ->
//                progressLiveData1.value = downloadProgress
//            })
            CoroutinesDownloadManager.getInstance().clearFiles()
            CoroutinesDownloadManager.getInstance().download(downloadUrlpng, {

            }, { downloadProgress ->
                progressLiveData.value = downloadProgress
            })

            CoroutinesDownloadManager.getInstance().multiDownload(listOf(downloadUrl, downloadUrlpng1), {

            }, { downloadProgress ->
                progressLiveData1.value = downloadProgress
            })
        }

    }
}