package com.catchpig.mvvm.network.download

import com.catchpig.mvvm.listener.DownloadProgressListener
import com.catchpig.mvvm.listener.MultiDownloadCallback
import io.reactivex.rxjava3.subscribers.ResourceSubscriber

/**
 *
 * @author catchpig
 * @date 2020/12/3 17:11
 */
class MultiDownloadSubscriber(private val multiDownloadCallback: MultiDownloadCallback):ResourceSubscriber<String>(),DownloadProgressListener {
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

    }
}