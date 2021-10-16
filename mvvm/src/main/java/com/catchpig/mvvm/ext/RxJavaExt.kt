package com.catchpig.mvvm.ext

import com.catchpig.mvvm.base.viewmodel.IBaseViewModel
import com.catchpig.utils.ext.logd
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subscribers.ResourceSubscriber

private const val TAG = "RxJavaExt"

/**
 * 被观察者在io线程中执行,观察者在主线程中执行
 */
fun <T> Flowable<T>.io2main(): Flowable<T> {
    return this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun <T> Flowable<T>.loadingView(
    iBaseViewModel: IBaseViewModel,
    callback: (t: T) -> Unit,
    error: ((t: Throwable) -> Unit)? = null,
    complete: (() -> Unit)? = null
): Disposable {
    return io2main().subscribeWith(object : ResourceSubscriber<T>() {
        override fun onStart() {
            super.onStart()
            iBaseViewModel.showLoadingView()
        }

        override fun onNext(t: T) {
            callback(t)
        }

        override fun onError(t: Throwable?) {
            t?.let { throwable ->
                iBaseViewModel.onError(throwable)
                error?.let {
                    it(throwable)
                }
                throwable.message?.let {
                    it.logd(TAG)
                }
            }
            iBaseViewModel.hideLoading()
        }

        override fun onComplete() {
            iBaseViewModel.hideLoading()
            complete?.let {
                it()
            }
        }
    })
}

fun <T> Flowable<T>.loadingDialog(
    iBaseViewModel: IBaseViewModel,
    callback: (t: T) -> Unit,
    error: ((t: Throwable) -> Unit)? = null,
    complete: (() -> Unit)? = null
): Disposable {
    return io2main().subscribeWith(object : ResourceSubscriber<T>() {
        override fun onStart() {
            super.onStart()
            iBaseViewModel.showLoadingDialog()
        }

        override fun onNext(t: T) {
            callback(t)
        }

        override fun onError(t: Throwable?) {
            t?.let { throwable ->
                iBaseViewModel.onError(throwable)
                error?.let {
                    it(throwable)
                }
                throwable.message?.let {
                    it.logd(TAG)
                }
            }
            iBaseViewModel.hideLoading()
        }

        override fun onComplete() {
            iBaseViewModel.hideLoading()
            complete?.let {
                it()
            }
        }
    })
}

fun <T> Flowable<T>.success(
    callback: (t: T) -> Unit,
    iBaseViewModel: IBaseViewModel? = null,
    error: ((t: Throwable) -> Unit)? = null,
    complete: (() -> Unit)? = null
): Disposable {
    return io2main().subscribeWith(object : ResourceSubscriber<T>() {

        override fun onNext(t: T) {
            callback(t)
        }

        override fun onError(t: Throwable?) {
            t?.let { throwable ->
                iBaseViewModel?.let {
                    it.onError(throwable)
                }
                error?.let {
                    it(throwable)
                }
                throwable.message?.let {
                    it.logd(TAG)
                }
            }
        }

        override fun onComplete() {
            complete?.let {
                it()
            }
        }
    })
}

fun <T> Flowable<T>.noSubscribe(iBaseViewModel: IBaseViewModel? = null): Disposable {
    return io2main().subscribeWith(object : ResourceSubscriber<T>() {

        override fun onNext(t: T) {
        }

        override fun onError(t: Throwable?) {
            t?.let { throwable ->
                iBaseViewModel?.let {
                    it.onError(throwable)
                }
                throwable.message?.let {
                    it.logd(TAG)
                }
            }
        }

        override fun onComplete() {

        }
    })
}
