package com.catchpig.kmvvm.network

import com.catchpig.annotation.ObserverError
import com.catchpig.mvvm.base.activity.BaseActivity
import com.catchpig.mvvm.base.fragment.BaseFragment
import com.catchpig.mvvm.interfaces.IObserverError

@ObserverError
class TokenObserverError : IObserverError {
    override fun onError(any: Any, t: Throwable) {


    }
}