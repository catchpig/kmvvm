package com.catchpig.kmvvm.apk.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.catchpig.download.entity.DownloadProgress
import com.catchpig.download.manager.CoroutinesDownloadManager
import com.catchpig.mvvm.base.viewmodel.BaseViewModel
import com.catchpig.utils.ext.logd
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

    companion object {
        private const val TAG = "InstallApkViewModel"
    }


    fun download() {
        val wandoujiaApk =
            "https://www.wandoujia.com/apps/7461948/binding?spm=aligames_platform_ug.wdj_seo.0.0.18887386H4PfNC&source=web_seo_baidu_binded"
        val yingyongbaoApl =
            "https://imtt.dd.qq.com/16891/myapp/channel_102497291_1165285_b0e7c10e19b54b31dfaf1fd4c0150e44.apk"
        val png_download = "https://img2.baidu.com/it/u=1003272215,1878948666&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800"
//        val wanAndroidApk =
//            "https://raw.githubusercontent.com/iceCola7/WanAndroid/master/app/release/WanAndroid-release.apk"
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
        CoroutinesDownloadManager.getInstance().clearFiles()
        viewModelScope.launch(Dispatchers.Main) {
            CoroutinesDownloadManager.getInstance().download(wandoujiaApk, {
                installApkLiveData.value = it
            }, { downloadProgress ->
                progressLiveData.value = downloadProgress
            })
        }

        viewModelScope.launch(Dispatchers.Main) {
            CoroutinesDownloadManager.getInstance()
                .multiDownload(listOf(png_download, yingyongbaoApl), {

                }, { downloadProgress ->
                    downloadProgress.toString().logd(TAG)
                    progressLiveData1.value = downloadProgress
                })
        }

    }
}