package com.catchpig.kotlin_mvvm.mvp.main.view

import android.view.View
import com.catchpig.annotation.ClickGap
import com.catchpig.annotation.MethodLog
import com.catchpig.annotation.StatusBar
import com.catchpig.annotation.enums.LEVEL
import com.catchpig.kotlin_mvvm.R
import com.catchpig.kotlin_mvvm.mvp.apk.view.InstallApkActivity
import com.catchpig.kotlin_mvvm.mvp.child.ChildActivity
import com.catchpig.kotlin_mvvm.mvp.fullscreen.FullScreenActivity
import com.catchpig.kotlin_mvvm.mvp.main.viewmodel.MainViewModel
import com.catchpig.kotlin_mvvm.mvp.recycle.RecycleActivity
import com.catchpig.kotlin_mvvm.mvp.transparent.TransparentActivity
import com.catchpig.mvvm.base.activity.BaseViewModelActivity
import com.catchpig.utils.ext.startKtActivity

@StatusBar(enabled = true)
class MainActivity : BaseViewModelActivity<MainViewModel>() {

    override fun initParam() {
    }

    @MethodLog(LEVEL.I)
    override fun initView() {


    }

    override fun layoutId(): Int {
        return R.layout.activity_main
    }

    @MethodLog(LEVEL.V)
    @ClickGap(2000)
    fun openChild(v:View){
        when (v.id) {
            R.id.is_title -> {
                startKtActivity<ChildActivity>()
            }
            R.id.transparent -> {
                startKtActivity<TransparentActivity>()
            }
            R.id.full_screen -> {
                startKtActivity<FullScreenActivity>()
            }
            R.id.recycle -> {
                startKtActivity<RecycleActivity>()
            }
            R.id.installApk ->{
                startKtActivity<InstallApkActivity>()
            }
            else -> {
            }
        }
    }
}
