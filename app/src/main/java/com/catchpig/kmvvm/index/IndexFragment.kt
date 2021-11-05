package com.catchpig.kmvvm.index

import android.os.Bundle
import android.view.View
import com.catchpig.kmvvm.R
import com.catchpig.kmvvm.apk.view.InstallApkActivity
import com.catchpig.kmvvm.child.ChildActivity
import com.catchpig.kmvvm.databinding.FragmentIndexBinding
import com.catchpig.kmvvm.fullscreen.FullScreenActivity
import com.catchpig.kmvvm.recycle.RecycleActivity
import com.catchpig.kmvvm.transparent.TransparentActivity
import com.catchpig.mvvm.base.fragment.BaseFragment
import com.catchpig.utils.ext.startKtActivity
import com.gyf.immersionbar.ktx.hideStatusBar
import com.gyf.immersionbar.ktx.immersionBar

class IndexFragment : BaseFragment<FragmentIndexBinding>(),View.OnClickListener {
    companion object {
        fun newInstance(): IndexFragment {
            return IndexFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        bodyBinding.run {
            openTitle.setOnClickListener(this@IndexFragment)
            transparent.setOnClickListener(this@IndexFragment)
            fullScreen.setOnClickListener(this@IndexFragment)
            recycle.setOnClickListener(this@IndexFragment)
            installApk.setOnClickListener(this@IndexFragment)
        }
    }

    override fun onStart() {
        super.onStart()
        immersionBar {
            transparentStatusBar()
            statusBarView()
            statusBarColor(R.color.white)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.open_title -> {
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