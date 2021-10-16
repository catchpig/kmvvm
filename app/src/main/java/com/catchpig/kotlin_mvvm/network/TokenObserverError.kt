package com.catchpig.kotlin_mvvm.network

import com.catchpig.annotation.ObserverError
import com.catchpig.mvvm.base.viewmodel.BaseViewModel
import com.catchpig.mvvm.interfaces.IObserverError

@ObserverError
class TokenObserverError : IObserverError {
    override fun onError(baseViewModel: BaseViewModel, t: Throwable) {

    }
}