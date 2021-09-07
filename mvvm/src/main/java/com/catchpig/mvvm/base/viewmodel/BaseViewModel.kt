package com.catchpig.mvvm.base.viewmodel

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.catchpig.mvvm.ext.io2main
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subscribers.ResourceSubscriber

open class BaseViewModel : ViewModel(), LifecycleObserver {
    private var mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreate() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
    }


    /**
     * 处理请求接口(线程安全,防止内存泄露)
     * @param callback
     * @param callback 回调函数
     * @param io2main 是否flowable在io线程中执行,callback在主线程中执行(默认为true)
     */
    fun <T> execute(flowable: Flowable<T>, callback: ResourceSubscriber<T>, io2main: Boolean = true): Disposable {
        val disposable = if (io2main) {
            flowable.io2main().subscribeWith(callback)
        } else {
            flowable.subscribeWith(callback)
        }
        mCompositeDisposable.add(disposable)
        return disposable
    }


    /**
     * 删除指定的Disposable
     */
    fun remove(disposable: Disposable) {
        mCompositeDisposable.remove(disposable)
    }
}