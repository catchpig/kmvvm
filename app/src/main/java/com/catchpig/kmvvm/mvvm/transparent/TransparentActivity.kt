package com.catchpig.kmvvm.mvvm.transparent

import com.bumptech.glide.Glide
import com.catchpig.annotation.StatusBar
import com.catchpig.kmvvm.databinding.ActivityTransparentBinding
import com.catchpig.mvvm.base.activity.BaseVMActivity

@StatusBar(transparent = true)
class TransparentActivity : BaseVMActivity<ActivityTransparentBinding, TransparentViewModel>() {
    override fun initParam() {

    }

    override fun initView() {
        viewModel.getBanner()
    }

    override fun initObserver() {
        viewModel.bannerLiveData.observe(this, {
            Glide.with(this).load(it.imagePath).into(bodyBinding.banner)
        })
    }
}
