package com.catchpig.kotlin_mvvm.mvvm.main.viewmodel

import com.catchpig.mvvm.base.viewmodel.BaseViewModel

/**
 * @author catchpig
 * @date 2019/8/18 00:18
 */
class MainViewModel : BaseViewModel() {
    override fun onCreate() {
        super.onCreate()
//        execute(model.banner(), object :Callback<Result<Any>>(){
//            override fun onNext(result: Result<Any>?) {
//                view.toast(result!!.errorCode.toString())
//            }
//        })
    }

    override fun onError(t: Throwable) {

    }
}