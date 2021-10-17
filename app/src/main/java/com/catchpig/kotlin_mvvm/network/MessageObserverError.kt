package com.catchpig.kotlin_mvvm.network

import com.catchpig.annotation.ObserverError
import com.catchpig.mvvm.base.viewmodel.IBaseViewModel
import com.catchpig.mvvm.interfaces.IObserverError

@ObserverError
class MessageObserverError : IObserverError {
    override fun onError(iBaseViewModel: IBaseViewModel, t: Throwable) {
        iBaseViewModel.sendMessage("错误消息")
    }
}