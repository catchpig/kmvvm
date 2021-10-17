package com.catchpig.mvvm.interfaces

import com.catchpig.mvvm.base.viewmodel.IBaseViewModel

interface IObserverError {
    fun onError(iBaseViewModel: IBaseViewModel, t: Throwable)
}