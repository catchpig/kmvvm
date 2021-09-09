package com.catchpig.mvvm.apt

import com.catchpig.mvvm.base.activity.BaseActivity

/**
 * @author catchpig
 * @date 2019/10/17 00:17
 */
interface MvpCompiler {
    /**
     * 初始化标题栏
     */
    fun inject(activity: BaseActivity<*>)

}