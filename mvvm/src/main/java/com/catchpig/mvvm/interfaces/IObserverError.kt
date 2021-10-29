package com.catchpig.mvvm.interfaces

interface IObserverError {
    fun onError(any: Any, t: Throwable)
}