package com.catchpig.kmvvm.main.view

import android.view.View
import com.catchpig.annotation.StatusBar
import com.catchpig.kmvvm.R
import com.catchpig.kmvvm.adapter.MainAdapter
import com.catchpig.kmvvm.apk.view.InstallApkActivity
import com.catchpig.kmvvm.child.ChildActivity
import com.catchpig.kmvvm.databinding.ActivityMainBinding
import com.catchpig.kmvvm.fullscreen.FullScreenActivity
import com.catchpig.kmvvm.main.viewmodel.MainViewModel
import com.catchpig.kmvvm.recycle.RecycleActivity
import com.catchpig.kmvvm.transparent.TransparentActivity
import com.catchpig.mvvm.base.activity.BaseVMActivity
import com.catchpig.utils.ext.startKtActivity

@StatusBar(enabled = true)
class MainActivity : BaseVMActivity<ActivityMainBinding, MainViewModel>() {

    private lateinit var mainAdapter: MainAdapter
    override fun initParam() {
    }

    override fun initView() {
        mainAdapter = MainAdapter(supportFragmentManager)
        bodyBinding.run {
            viewPager.adapter = mainAdapter
            tabLayout.setupWithViewPager(viewPager)
        }
    }

    override fun initFlow() {

    }
}
