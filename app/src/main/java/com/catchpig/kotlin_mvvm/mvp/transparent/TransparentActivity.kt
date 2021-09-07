package com.catchpig.kotlin_mvvm.mvp.transparent

import com.catchpig.annotation.StatusBar
import com.catchpig.kotlin_mvvm.R
import com.catchpig.mvvm.base.activity.BaseActivity

@StatusBar(transparent = true)
class TransparentActivity : BaseActivity() {
    override fun layoutId(): Int {
        return R.layout.activity_transparent
    }
}
