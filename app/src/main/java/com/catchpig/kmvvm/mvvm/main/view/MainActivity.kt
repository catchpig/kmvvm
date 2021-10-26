package com.catchpig.kmvvm.mvvm.main.view

import android.view.View
import com.catchpig.annotation.StatusBar
import com.catchpig.kmvvm.R
import com.catchpig.kmvvm.databinding.ActivityMainBinding
import com.catchpig.kmvvm.mvvm.apk.view.InstallApkActivity
import com.catchpig.kmvvm.mvvm.child.ChildActivity
import com.catchpig.kmvvm.mvvm.fullscreen.FullScreenActivity
import com.catchpig.kmvvm.mvvm.main.viewmodel.MainViewModel
import com.catchpig.kmvvm.mvvm.recycle.RecycleActivity
import com.catchpig.kmvvm.mvvm.transparent.TransparentActivity
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
