package com.catchpig.kmvvm.fullscreen

import android.os.Bundle
import com.catchpig.annotation.StatusBar
import com.catchpig.kmvvm.databinding.ActivityFullScreenBinding
import com.catchpig.mvvm.base.activity.BaseActivity

@StatusBar(hide = true)
class FullScreenActivity : BaseActivity<ActivityFullScreenBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bodyBinding.root.setOnClickListener {
            loadingDialog()
            it.postDelayed({
                hideLoading()
            }, 5000)
        }
    }
}
