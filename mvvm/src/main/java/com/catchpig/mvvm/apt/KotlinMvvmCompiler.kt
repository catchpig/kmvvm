package com.catchpig.mvvm.apt

import android.app.Activity
import android.view.ViewGroup
import com.catchpig.mvvm.apt.interfaces.ActivityCompiler
import com.catchpig.mvvm.apt.interfaces.GlobalCompiler
import com.catchpig.mvvm.apt.interfaces.RecyclerAdapterCompiler
import com.catchpig.mvvm.apt.interfaces.ServiceApiCompiler
import com.catchpig.mvvm.base.adapter.RecyclerAdapter
import com.catchpig.mvvm.entity.AdapterBinding
import com.catchpig.mvvm.entity.ServiceParam
import com.catchpig.mvvm.exception.AptAdapterException
import com.catchpig.mvvm.interfaces.IGlobalConfig
import com.catchpig.utils.ext.logd
import okhttp3.ResponseBody
import retrofit2.Converter
import java.lang.reflect.Type

/**
 * @author catchpig
 * @date 2019/10/17 00:17
 */
object KotlinMvvmCompiler {
    private const val TAG = "KotlinMvvmCompiler"
    private val globalCompiler: GlobalCompiler by lazy {
        var compilerClass = Class.forName("com.catchpig.mvvm.apt.interfaces.Global_Compiler")
        compilerClass.newInstance() as GlobalCompiler
    }

    private val serviceApiCompiler: ServiceApiCompiler by lazy {
        var compilerClass = Class.forName("com.catchpig.mvvm.apt.interfaces.ServiceApi_Compiler")
        compilerClass.newInstance() as ServiceApiCompiler
    }

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

    fun onError(any: Any, t: Throwable) {
        globalCompiler.onError(any, t)
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
        return globalCompiler.getGlobalConfig()
    }

    fun getServiceParam(className: String): ServiceParam {
        return serviceApiCompiler.getServiceParam(className)
    }

    fun getResponseBodyConverter(
        className: String,
        type: Type
    ): Converter<ResponseBody, Any>? {
        return serviceApiCompiler.getResponseBodyConverter(className, type)
    }
}