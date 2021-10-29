package com.catchpig.kmvvm.network

import com.catchpig.annotation.ObserverError
import com.catchpig.mvvm.base.activity.BaseActivity
import com.catchpig.mvvm.base.fragment.BaseFragment
import com.catchpig.mvvm.interfaces.IObserverError

@ObserverError
class MessageObserverError : IObserverError {
    override fun onError(any: Any, t: Throwable) {
        t.message?.let {
            when (any) {
                is BaseActivity<*> -> {
                    any.snackBar(it)
                }
                is BaseFragment<*> -> {
                    any.snackBar(it)
                }
                else -> {
                }
            }
        }
    }
}