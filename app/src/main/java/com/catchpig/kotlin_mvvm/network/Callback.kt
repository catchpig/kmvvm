package com.catchpig.kotlin_mvvm.network

import com.catchpig.utils.ext.loge
import io.reactivex.rxjava3.subscribers.ResourceSubscriber

abstract class Callback<T>:ResourceSubscriber<T>() {
    override fun onError(t: Throwable?) {
        t!!.message!!.loge()
    }

    override fun onComplete() {

    }
}