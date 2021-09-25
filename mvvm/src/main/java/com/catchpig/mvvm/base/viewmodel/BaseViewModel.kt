package com.catchpig.mvvm.base.viewmodel

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.catchpig.mvvm.base.callback.BaseCallback
import com.catchpig.mvvm.ext.io2main
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subscribers.ResourceSubscriber
import java.lang.Deprecated

abstract class BaseViewModel : ViewModel(), IBaseViewModel {
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    var showLoadingLiveData = MutableLiveData<Boolean>()
        private set
    var hideLoadingLiveData = MutableLiveData<Boolean>()
        private set
    var toastLiveData = MutableLiveData<String>()
        private set

    /**
     * 展现loading
     */
    override fun showLoading(isDialog: Boolean) {
        showLoadingLiveData.value = isDialog
    }

    /**
     * 隐藏loading
     */
    override fun hideLoading() {
        hideLoadingLiveData.value = true
    }

    /**
     * 处理请求接口(线程安全,防止内存泄露)
     * @param callback
     * @param callback 回调函数
     * @param io2main 是否flowable在io线程中执行,callback在主线程中执行(默认为true)
     */
    @Deprecated
    fun <T> execute(
        flowable: Flowable<T>,
        callback: ResourceSubscriber<T>,
        io2main: Boolean = true
    ): Disposable {
        val disposable = if (io2main) {
            flowable.io2main().subscribeWith(callback)
        } else {
            flowable.subscribeWith(callback)
        }
        compositeDisposable.add(disposable)
        return disposable
    }

    @Deprecated
    open fun <T> execute(
        flowable: Flowable<T>,
        io2main: Boolean = true,
        callback: (t: T) -> Unit
    ): Disposable {
        return execute(flowable, object : BaseCallback<T>() {
            override fun onSuccess(t: T) {
                callback(t)
            }
        }, io2main)
    }

    /**
     * 添加Disposable到CompositeDisposable
     */
    override fun add(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    /**
     * 删除指定的Disposable
     */
    override fun remove(disposable: Disposable) {
        compositeDisposable.remove(disposable)
    }

    override fun onAny(owner: LifecycleOwner?, event: Lifecycle.Event?) {

    }

    override fun onCreate() {

    }

    override fun onStart() {

    }

    override fun onResume() {

    }

    override fun onPause() {

    }

    override fun onStop() {

    }

    override fun onDestroy() {

    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}