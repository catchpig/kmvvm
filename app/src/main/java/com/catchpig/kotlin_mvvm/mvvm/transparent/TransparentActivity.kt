package com.catchpig.kotlin_mvvm.mvvm.transparent

import com.catchpig.annotation.StatusBar
import com.catchpig.kotlin_mvvm.databinding.ActivityTransparentBinding
import com.catchpig.mvvm.base.activity.BaseActivity

@StatusBar(transparent = true)
class TransparentActivity : BaseActivity<ActivityTransparentBinding>() {

}
