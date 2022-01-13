package com.catchpig.mvvm.network.download

import com.catchpig.mvvm.listener.DownloadProgressListener
import com.catchpig.mvvm.listener.MultiDownloadCallback
import com.catchpig.utils.ext.logd
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.subscribers.ResourceSubscriber

/**
 *
 * @author catchpig
 * @date 2020/12/3 17:11
 */
class MultiDownloadSubscriber(
    private val totalCount: Int,
    private val multiDownloadCallback: MultiDownloadCallback
) : ResourceSubscriber<String>(), DownloadProgressListener {
    companion object {
        private const val TAG = "MultiDownloadSubscriber"
    }

    private var paths = arrayListOf<String>()
    override fun onStart() {
        super.onStart()
        multiDownloadCallback.onStart()
    }

    override fun onNext(t: String) {
        paths.add(t)
    }

    override fun onError(t: Throwable?) {
        t?.let {
            multiDownloadCallback.onError(it)
        }
    }

    override fun onComplete() {
        multiDownloadCallback.onSuccess(paths)
        multiDownloadCallback.onComplete()
    }

    override fun update(read: Long, count: Long, done: Boolean) {
        Flowable.just(done).subscribeOn(AndroidSchedulers.mainThread()).subscribe(Consumer {
            "progress:$read/$count;$done,completeCount:${paths.size},totalCount:$totalCount".logd(TAG)
            multiDownloadCallback.onProgress(read, count, paths.size, totalCount)
        })
    }
}