package com.catchpig.mvvm.base.viewmodel

import androidx.annotation.CallSuper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.catchpig.mvvm.apt.KotlinMvvmCompiler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

/**
 *
 */
open class BaseViewModel : ViewModel(), IBaseViewModel {
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    var showLoadingLiveData = MutableLiveData<Boolean>()
        private set
    var hideLoadingLiveData = MutableLiveData<Boolean>()
        private set
    var toastLiveData = MutableLiveData<String>()
        private set

    /**
     * 展现loadingView
     */
    override fun showLoadingView() {
        showLoadingLiveData.value = false
    }

    /**
     * 展现loadingDialog
     */
    override fun showLoadingDialog() {
        showLoadingLiveData.value = true
    }

    /**
     * 隐藏loading
     */
    override fun hideLoading() {
        hideLoadingLiveData.value = true
    }

    @CallSuper
    override fun onError(t: Throwable) {
        KotlinMvvmCompiler.onError(this, t)
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