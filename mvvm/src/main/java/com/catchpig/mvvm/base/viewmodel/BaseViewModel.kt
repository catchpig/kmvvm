package com.catchpig.mvvm.base.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

/**
 *
 */
open class BaseViewModel : ViewModel(), IBaseViewModel {
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    /**
     * 添加Disposable到CompositeDisposable
     * @param disposable Disposable
     */
    override fun add(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    /**
     * 删除指定的Disposable
     * @param disposable Disposable
     */
    override fun remove(disposable: Disposable) {
        compositeDisposable.remove(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}