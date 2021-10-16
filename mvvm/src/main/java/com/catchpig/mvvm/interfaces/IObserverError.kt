package com.catchpig.mvvm.interfaces

import com.catchpig.mvvm.base.viewmodel.BaseViewModel

interface IObserverError {
    fun onError(baseViewModel: BaseViewModel, t: Throwable)
}