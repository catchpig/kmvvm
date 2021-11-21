package com.catchpig.kmvvm.index

import android.view.Gravity
import android.view.View
import com.bumptech.glide.Glide
import com.catchpig.kmvvm.R
import com.catchpig.kmvvm.apk.view.InstallApkActivity
import com.catchpig.kmvvm.child.ChildActivity
import com.catchpig.kmvvm.databinding.FragmentIndexBinding
import com.catchpig.kmvvm.fullscreen.FullScreenActivity
import com.catchpig.kmvvm.recycle.RecycleActivity
import com.catchpig.kmvvm.transparent.TransparentActivity
import com.catchpig.mvvm.base.fragment.BaseVMFragment
import com.catchpig.utils.ext.startKtActivity

class IndexFragment : BaseVMFragment<FragmentIndexBinding, IndexViewModel>(), View.OnClickListener {
    companion object {
        fun newInstance(): IndexFragment {
            return IndexFragment()
        }
    }

    override fun initParam() {

    }

    override fun initView() {
        bodyBinding.run {
            banner.bindLifecycle(this@IndexFragment)
            banner.setAutoPlay(true)
            banner.setInfiniteLoop(true)
            banner.setImageLoader { imageView, path ->
                Glide.with(this@IndexFragment).load(path).into(imageView)
            }
            snackbar.setOnClickListener {
                snackBar("dadad")
            }
            openTitle.setOnClickListener(this@IndexFragment)
            transparent.setOnClickListener(this@IndexFragment)
            fullScreen.setOnClickListener(this@IndexFragment)
            recycle.setOnClickListener(this@IndexFragment)
            installApk.setOnClickListener(this@IndexFragment)
        }
    }

    override fun initFlow() {
        lifecycleFlowLoadingView(viewModel.queryBanners()) {
            val images = mutableListOf<String>()
            this.forEach {
                images.add(it.imagePath)
            }
            bodyBinding.banner.run {
                setImages(images)
                start()
            }
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