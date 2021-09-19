package com.catchpig.kotlin_mvvm.mvp.apk.viewmodel

import androidx.lifecycle.MutableLiveData
import com.catchpig.mvvm.base.viewmodel.BaseViewModel
import com.catchpig.mvvm.manager.DownloadManager

/**
 *
 * @author catchpig
 * @date 2020/11/20 15:51
 */
class InstallApkViewModel : BaseViewModel() {
    val progressLiveData = MutableLiveData<Int>()
    override fun onError(t: Throwable) {

    }

    fun download() {
        val downloadUrl = "http://www.wichot.com/Uploads/download.pdf"
//        val downloadUrl = "https://wanandroid.com/blogimgs/2d120094-e1ee-47fb-a155-6eb4ca49d01f.apk"
        DownloadManager.download(downloadUrl, {

        }, { readLength, countLength ->
            progressLiveData.value = (readLength * 100 / countLength).toInt()
        })
//        val imageUrl = "https://tvax4.sinaimg.cn/large/005BYqpgly1g1usrl6vysj31c00u0k6p.jpg"
//        model.download(imageUrl, object : DownloadCallback {
//            override fun onStart() {
//
//            }
//
//            override fun onComplete() {
//
//            }
//
//            override fun onSuccess(path: String) {
//
//            }
//
//            override fun onProgress(readLength: Long, countLength: Long) {
//                view.setDownloadProgress1((readLength * 100 / countLength).toInt())
//            }
//
//            override fun onError(t: Throwable) {
//                println(t.message)
//            }
//        })

//        val urls = arrayListOf<String>()
//        for (i in 0..1){
//            urls.add(downloadUrl)
//        }
//        model.multiDownload(urls,object :MultiDownloadCallback{
//
//            override fun onSuccess(paths: MutableList<String>) {
//                paths.toString().logi()
//            }
//
//        })
    }
}