package com.catchpig.kmvvm.apk.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.catchpig.download.entity.DownloadProgress
import com.catchpig.download.manager.CoroutinesDownloadManager
import com.catchpig.download.manager.RxJavaDownloadManager
import com.catchpig.mvvm.base.viewmodel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *
 * @author catchpig
 * @date 2020/11/20 15:51
 */
class InstallApkViewModel : BaseViewModel() {
    val installApkLiveData = MutableLiveData<String>()
    val progressLiveData = MutableLiveData<DownloadProgress>()
    val progressLiveData1 = MutableLiveData<DownloadProgress>()


    fun download() {
        val wandoujiaApk = "https://www.wandoujia.com/apps/7461948/binding?spm=aligames_platform_ug.wdj_seo.0.0.18887386H4PfNC&source=web_seo_baidu_binded"
        val yingyongbaoApl =
            "https://imtt.dd.qq.com/16891/myapp/channel_102497291_1165285_b0e7c10e19b54b31dfaf1fd4c0150e44.apk"
        val wanAndroidApk = "https://wanandroid.com/blogimgs/2d120094-e1ee-47fb-a155-6eb4ca49d01f.apk"
//        RxJavaDownloadManager.getInstance().clearFiles()
//        RxJavaDownloadManager.getInstance().download(wandoujiaApk, {
//            installApkLiveData.value = it
//        }, { downloadProgress ->
//            progressLiveData.value = downloadProgress
//        })
//
//        RxJavaDownloadManager.getInstance().multiDownload(listOf(wanAndroidApk, yingyongbaoApl), {
//
//        }, { downloadProgress ->
//            progressLiveData1.value = downloadProgress
//        })
        viewModelScope.launch(Dispatchers.Main) {
            CoroutinesDownloadManager.getInstance().clearFiles()
            CoroutinesDownloadManager.getInstance().download(wandoujiaApk, {
                installApkLiveData.value = it
            }, { downloadProgress ->
                progressLiveData.value = downloadProgress
            })

            CoroutinesDownloadManager.getInstance()
                .multiDownload(listOf(wanAndroidApk, yingyongbaoApl), {

                }, { downloadProgress ->
                    progressLiveData1.value = downloadProgress
                })
        }

    }
}