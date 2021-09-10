package com.catchpig.mvvm.apt

import android.app.Activity

/**
 * @author catchpig
 * @date 2019/10/17 00:17
 */
interface MvvmCompiler {
    /**
     * 初始化标题栏
     */
    fun inject(activity: Activity)

}