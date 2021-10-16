package com.catchpig.mvvm.apt.interfaces

import android.app.Activity

/**
 * @author catchpig
 * @date 2019/10/17 00:17
 */
interface ActivityCompiler {
    /**
     * 初始化标题栏和状态栏
     */
    fun inject(activity: Activity)

}