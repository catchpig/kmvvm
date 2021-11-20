package com.catchpig.kmvvm.network

import com.catchpig.annotation.FlowError
import com.catchpig.mvvm.base.activity.BaseActivity
import com.catchpig.mvvm.base.fragment.BaseFragment
import com.catchpig.mvvm.interfaces.IFlowError

@FlowError
class MessageFlowError : IFlowError {
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