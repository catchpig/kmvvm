package com.catchpig.kotlin_mvvm.mvp.main.viewmodel

import com.catchpig.mvvm.base.viewmodel.BaseViewModel
import com.catchpig.mvvm.base.callback.BaseCallback
import io.reactivex.rxjava3.core.Flowable
import java.util.concurrent.TimeUnit

/**
 * @author catchpig
 * @date 2019/8/18 00:18
 */
class MainViewModel : BaseViewModel() {
    override fun onCreate() {
        super.onCreate()
        execute(Flowable.timer(5,TimeUnit.SECONDS),object : BaseCallback<Long>(this){
            override fun onSuccess(t: Long) {

            }
        })
//        execute(model.banner(), object :Callback<Result<Any>>(){
//            override fun onNext(result: Result<Any>?) {
//                view.toast(result!!.errorCode.toString())
//            }
//        })
    }
}