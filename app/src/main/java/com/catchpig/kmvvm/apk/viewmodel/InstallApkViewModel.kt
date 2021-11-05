package com.catchpig.kmvvm.apk.viewmodel

import androidx.lifecycle.MutableLiveData
import com.catchpig.mvvm.base.viewmodel.BaseViewModel
import com.catchpig.mvvm.network.manager.DownloadManager

/**
 *
 * @author catchpig
 * @date 2020/11/20 15:51
 */
class InstallApkViewModel : BaseViewModel() {
    val progressLiveData = MutableLiveData<Int>()

    fun download() {
//        val downloadUrl = "http://www.wichot.com/Uploads/download.pdf"
        val downloadUrl = "https://wanandroid.com/blogimgs/2d120094-e1ee-47fb-a155-6eb4ca49d01f.apk"
        DownloadManager.download(downloadUrl, {

        }, { readLength, countLength ->
            progressLiveData.value = (readLength * 100 / countLength).toInt()
        })
    }
}