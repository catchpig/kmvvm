package com.catchpig.kotlin_mvvm.mvvm.main.view

import android.view.View
import com.catchpig.annotation.StatusBar
import com.catchpig.kotlin_mvvm.R
import com.catchpig.kotlin_mvvm.databinding.ActivityMainBinding
import com.catchpig.kotlin_mvvm.mvvm.apk.view.InstallApkActivity
import com.catchpig.kotlin_mvvm.mvvm.child.ChildActivity
import com.catchpig.kotlin_mvvm.mvvm.fullscreen.FullScreenActivity
import com.catchpig.kotlin_mvvm.mvvm.main.viewmodel.MainViewModel
import com.catchpig.kotlin_mvvm.mvvm.recycle.RecycleActivity
import com.catchpig.kotlin_mvvm.mvvm.transparent.TransparentActivity
import com.catchpig.mvvm.base.activity.BaseVMActivity
import com.catchpig.utils.ext.startKtActivity

@StatusBar(enabled = true)
class MainActivity : BaseVMActivity<ActivityMainBinding, MainViewModel>() {

    override fun initParam() {
    }

    override fun initView() {
    }

    override fun initObserver() {

    }

    fun openChild(v: View) {
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
            R.id.installApk -> {
                startKtActivity<InstallApkActivity>()
            }
            else -> {
            }
        }
    }
}
