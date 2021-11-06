package com.catchpig.kmvvm.transparent

import com.bumptech.glide.Glide
import com.catchpig.annotation.StatusBar
import com.catchpig.kmvvm.databinding.ActivityTransparentBinding
import com.catchpig.mvvm.base.activity.BaseVMActivity
import kotlinx.coroutines.flow.*

@StatusBar(transparent = true)
class TransparentActivity : BaseVMActivity<ActivityTransparentBinding, TransparentViewModel>() {
    override fun initParam() {

    }

    override fun initView() {

    }

    override fun initFlow() {
        lifecycleLoadingDialog(viewModel.banner()) {
            Glide.with(this@TransparentActivity).load(imagePath).into(bodyBinding.banner)
            snackBar(title)
        }
    }
}
