package com.catchpig.download.subscriber

import com.catchpig.download.callback.DownloadCallback
import com.catchpig.download.callback.DownloadProgressListener
import com.catchpig.download.entity.DownloadProgress
import com.catchpig.utils.ext.logd
import com.catchpig.utils.ext.loge
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.subscribers.ResourceSubscriber

/**
 * 下载观察者
 * @author catchpig
 * @date 2020/11/20 10:25
 */
internal class DownloadSubscriber(private val downloadCallback: DownloadCallback) :
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
            "onError(${it})".loge(TAG)
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