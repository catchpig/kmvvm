package com.catchpig.kmvvm.network

import com.catchpig.annotation.FlowError
import com.catchpig.mvvm.base.activity.BaseActivity
import com.catchpig.mvvm.base.fragment.BaseFragment
import com.catchpig.mvvm.base.view.BaseView
import com.catchpig.mvvm.interfaces.IFlowError

@FlowError
class MessageFlowError : IFlowError {
    override fun onBaseFragmentError(baseFragment: BaseFragment<*>, t: Throwable) {

    }

    override fun onBaseActivityError(baseActivity: BaseActivity<*>, t: Throwable) {

    }

    override fun onError(any: Any, t: Throwable) {
        t.printStackTrace()
        t.message?.let {
            when (any) {
                is BaseView -> {
                    any.snackBar(it)
                }
                else -> {
                }
            }
        }
    }
}