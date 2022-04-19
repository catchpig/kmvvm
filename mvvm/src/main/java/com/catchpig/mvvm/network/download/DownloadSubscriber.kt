package com.catchpig.mvvm.network.download

import com.catchpig.mvvm.entity.DownloadProgress
import com.catchpig.mvvm.listener.DownloadCallback
import com.catchpig.mvvm.listener.DownloadProgressListener
import com.catchpig.utils.ext.logd
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.subscribers.ResourceSubscriber

/**
 * 下载观察者
 * @author catchpig
 * @date 2020/11/20 10:25
 */
class DownloadSubscriber(private val downloadCallback: DownloadCallback) :
    ResourceSubscriber<String>(), DownloadProgressListener {
    companion object {
        const val TAG = "DownloadSubscriber"
    }

    public override fun onStart() {
        super.onStart()
        "onStart".logd(TAG)
        downloadCallback.onStart()
    }

    override fun onNext(t: String) {
        "onNext($t)".logd(TAG)
        downloadCallback.onSuccess(t)
    }

    override fun onError(t: Throwable?) {
        t?.let {
            if (t is Exception) {
                t.printStackTrace()
            }
            "onError(${it})".logd(TAG)
            downloadCallback.onError(it)
        }
    }

    override fun onComplete() {
        "onComplete".logd(TAG)
        downloadCallback.onComplete()
    }

    override fun update(read: Long, count: Long, done: Boolean) {
        Flowable.just(done).subscribeOn(AndroidSchedulers.mainThread()).subscribe {
            val downloadProgress = if (done) {
                DownloadProgress(read, count, 1, 1)
            } else {
                DownloadProgress(read, count, 0, 1)
            }
            "update($read,$count,$done)".logd(TAG)
            downloadCallback.onProgress(downloadProgress)
        }

    }
}