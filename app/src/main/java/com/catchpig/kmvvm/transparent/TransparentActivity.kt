package com.catchpig.kmvvm.transparent

import com.bumptech.glide.Glide
import com.catchpig.annotation.StatusBar
import com.catchpig.kmvvm.databinding.ActivityTransparentBinding
import com.catchpig.mvvm.base.activity.BaseVMActivity
import com.catchpig.mvvm.ext.lifecycleLoadingDialog

@StatusBar(transparent = true)
class TransparentActivity : BaseVMActivity<ActivityTransparentBinding, TransparentViewModel>() {
    override fun initParam() {

    }

    override fun initView() {

    }

    override fun initFlow() {
        viewModel.banner().lifecycleLoadingDialog(this) {
            Glide.with(this@TransparentActivity).load(imagePath).into(bodyBinding.banner)
            snackBar(title)
        }
    }
}
