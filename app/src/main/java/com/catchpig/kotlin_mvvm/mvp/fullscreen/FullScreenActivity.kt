package com.catchpig.kotlin_mvvm.mvp.fullscreen

import com.catchpig.kotlin_mvvm.R
import com.catchpig.annotation.StatusBar
import com.catchpig.mvvm.base.activity.BaseActivity

@StatusBar(hide = true)
class FullScreenActivity : BaseActivity() {
    override fun layoutId(): Int {
        return R.layout.activity_full_screen
    }

}
