package com.catchpig.utils.ext

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers

private const val TAG = "RxJavaExt"

/**
 * 被观察者在io线程中执行,观察者在主线程中执行
 */
fun <T : Any> Flowable<T>.io2main(): Flowable<T> {
    return this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}
