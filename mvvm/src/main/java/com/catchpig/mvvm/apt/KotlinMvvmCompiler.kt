package com.catchpig.mvvm.apt

import android.app.Activity
import android.view.ViewGroup
import com.catchpig.mvvm.apt.interfaces.ActivityCompiler
import com.catchpig.mvvm.apt.interfaces.GlobalConfigCompiler
import com.catchpig.mvvm.apt.interfaces.RecyclerAdapterCompiler
import com.catchpig.mvvm.apt.interfaces.ViewModelCompiler
import com.catchpig.mvvm.base.adapter.RecyclerAdapter
import com.catchpig.mvvm.base.viewmodel.BaseViewModel
import com.catchpig.mvvm.entity.AdapterBinding
import com.catchpig.mvvm.exception.AptAdapterException
import com.catchpig.mvvm.interfaces.IGlobalConfig
import com.catchpig.utils.ext.logd

/**
 * @author catchpig
 * @date 2019/10/17 00:17
 */
object KotlinMvvmCompiler {
    private const val TAG = "KotlinMvvmCompiler"
    private var viewModelCompiler: ViewModelCompiler? = null
    private var globalConfigCompiler: GlobalConfigCompiler? = null
    fun inject(baseActivity: Activity) {
        val className = baseActivity.javaClass.name
        try {
            val compilerClass = Class.forName("${className}_Compiler")
            compilerClass.let {
                val activityCompiler = compilerClass.newInstance() as ActivityCompiler
                activityCompiler.inject(baseActivity)
            }
        } catch (exception: ClassNotFoundException) {
            "$className:没有被(com.catchpig.annotation)下编译时注解修饰".logd(TAG)
        }
    }

    fun onError(baseViewModel: BaseViewModel, t: Throwable) {
        if (viewModelCompiler == null) {
            try {
                val compilerClass =
                        Class.forName("com.catchpig.mvvm.base.viewmodel.ViewModel_Compiler")
                viewModelCompiler = compilerClass.newInstance() as ViewModelCompiler
            } catch (exception: ClassNotFoundException) {
                "没有使用注解ObserverError".logd(TAG)
            }
        }
        viewModelCompiler?.onError(baseViewModel, t)
    }

    fun viewBanding(recyclerAdapter: RecyclerAdapter<*, *>, parent: ViewGroup): AdapterBinding {
        val className = recyclerAdapter.javaClass.name
        try {
            val compilerClass = Class.forName("${className}_Compiler")
            compilerClass.let {
                val recyclerAdapterCompiler = compilerClass.newInstance() as RecyclerAdapterCompiler
                return recyclerAdapterCompiler.viewBanding(parent)
            }
        } catch (exception: ClassNotFoundException) {
            val msg = "${className}必须使用注解Adapter"
            msg.logd(TAG)
            throw AptAdapterException(msg)
        }
    }

    fun globalConfig(): IGlobalConfig {
        if (globalConfigCompiler == null) {
            val compilerClass =
                    Class.forName("com.catchpig.mvvm.interfaces.GlobalConfig_Compiler")
            globalConfigCompiler = compilerClass.newInstance() as GlobalConfigCompiler
        }
        return globalConfigCompiler!!.getGlobalConfig()
    }
}