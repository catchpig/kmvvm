package com.catchpig.mvvm.apt

import android.app.Activity
import com.catchpig.mvvm.base.activity.BaseActivity
import com.catchpig.utils.ext.logd

/**
 * @author catchpig
 * @date 2019/10/17 00:17
 */
object KotlinMvvmCompiler {
    private const val TAG = "KotlinMvvmCompiler"
    fun inject(baseActivity: Activity) {
        val className = baseActivity.javaClass.name
        try {
            val compilerClass = Class.forName(className + "_MvvmCompiler")
            compilerClass.let {
                val mvpCompiler = compilerClass.newInstance() as MvvmCompiler
                mvpCompiler.inject(baseActivity)
            }
        } catch (exception: ClassNotFoundException) {
            "$className:没有被(com.catchpig.annotation)下编译时注解修饰".let {
                it.logd(TAG)
            }
        }

    }
}