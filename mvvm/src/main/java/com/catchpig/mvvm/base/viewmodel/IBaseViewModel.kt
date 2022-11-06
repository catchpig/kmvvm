package com.catchpig.mvvm.base.viewmodel

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import io.reactivex.rxjava3.disposables.Disposable

/**
 * @author litao
 * date 2021/9/13
 * description:
 */
interface IBaseViewModel : DefaultLifecycleObserver, LifecycleEventObserver {
    /**
     * 添加disposable到CompositeDisposable
     * @param disposable Disposable
     */
    fun add(disposable: Disposable)

    /**
     * 从CompositeDisposable中删除一个disposable
     * @param disposable Disposable
     */
    fun remove(disposable: Disposable)

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        onAny(source, event)
    }

    /**
     * 此函数可以监听Activity的全部Event事件
     * @param owner
     * @param event
     */
    fun onAny(owner: LifecycleOwner?, event: Lifecycle.Event?) {}

    override fun onCreate(owner: LifecycleOwner) {
        onCreate()
    }

    fun onCreate() {}

    override fun onStart(owner: LifecycleOwner) {
        onStart()
    }

    fun onStart() {}

    override fun onResume(owner: LifecycleOwner) {
        onResume()
    }

    fun onResume() {}

    override fun onPause(owner: LifecycleOwner) {
        onPause()
    }

    fun onPause() {}

    override fun onStop(owner: LifecycleOwner) {
        onStop()
    }

    fun onStop() {}

    override fun onDestroy(owner: LifecycleOwner) {
        onDestroy()
    }

    fun onDestroy() {}
}