package com.catchpig.kmvvm.network

import com.catchpig.annotation.FlowError
import com.catchpig.mvvm.interfaces.IFlowError

@FlowError
class TokenFlowError : IFlowError {
    override fun onError(any: Any, t: Throwable) {


    }
}