package com.catchpig.download.subscriber

import android.os.Handler
import android.os.Looper
import com.catchpig.download.callback.DownloadProgressListener
import com.catchpig.download.callback.MultiDownloadCallback
import com.catchpig.download.entity.DownloadProgress
import com.catchpig.utils.ext.logd
import com.catchpig.utils.ext.loge
import io.reactivex.rxjava3.subscribers.ResourceSubscriber

/**
 *
 * @author catchpig
 * @date 2020/12/3 17:11
 */
internal class MultiDownloadSubscriber(
    private val totalCount: Int,
    private val multiDownloadCallback: MultiDownloadCallback
) : ResourceSubscriber<String>(), DownloadProgressListener {
    companion object {
        private const val TAG = "MultiDownloadSubscriber"
    }

    private val handler = Handler(Looper.getMainLooper())

    private var paths = arrayListOf<String>()
    public override fun onStart() {
        super.onStart()
        "onStart".logd(TAG)
        multiDownloadCallback.onStart()
    }

    override fun onNext(t: String) {
        "onNext($t)".logd(TAG)
        paths.add(t)
    }

    override fun onError(t: Throwable?) {
        t?.let {
            if (t is Exception) {
                t.printStackTrace()
            }
            "onError($t)".loge(TAG)
            multiDownloadCallback.onError(it)
        }
    }

    override fun onComplete() {
        "onComplete($paths)".logd(TAG)
        multiDownloadCallback.onSuccess(paths)
        multiDownloadCallback.onComplete()
    }

    override fun update(read: Long, count: Long, done: Boolean) {
        handler.post {
            val completeCount = paths.size + 1
            val downloadProgress = if (done && completeCount == totalCount) {
                DownloadProgress(read, count, totalCount, totalCount)
            } else {
                DownloadProgress(read, count, completeCount, totalCount)
            }
            "update->read:$read,count:$count,done:$done,completeCount:$completeCount,totalCount:$totalCount".logd(
                TAG
            )
            multiDownloadCallback.onProgress(downloadProgress)
        }
    }
}