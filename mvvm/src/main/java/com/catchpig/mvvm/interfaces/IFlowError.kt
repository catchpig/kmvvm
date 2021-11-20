package com.catchpig.mvvm.interfaces

interface IFlowError {
    fun onError(any: Any, t: Throwable)
}