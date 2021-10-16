package com.catchpig.kotlin_mvvm.mvvm.child

import com.catchpig.mvvm.base.viewmodel.BaseViewModel
import com.catchpig.mvvm.ext.loadingDialog
import com.catchpig.mvvm.ext.loadingView
import com.catchpig.mvvm.ext.noSubscribe
import com.catchpig.mvvm.ext.success
import io.reactivex.rxjava3.core.Flowable
import java.util.concurrent.TimeUnit

class ChildViewModel : BaseViewModel() {
    override fun onError(t: Throwable) {

    }

    fun loadingView() {
        Flowable.timer(5, TimeUnit.SECONDS).loadingView(this, {

        })
        Flowable.timer(5, TimeUnit.SECONDS).success({

        }, this)
        Flowable.timer(5, TimeUnit.SECONDS).noSubscribe()
    }

    fun loadingDialog() {
        Flowable.timer(5, TimeUnit.SECONDS).loadingDialog(this, {

        })
    }
}