package com.catchpig.mvvm.apt.interfaces

import com.catchpig.mvvm.entity.ServiceParam
import okhttp3.ResponseBody
import retrofit2.Converter
import java.lang.reflect.Type

interface ServiceApiCompiler {
    fun getServiceParam(className: String): ServiceParam

    fun getResponseBodyConverter(
        className: String,
        type: Type
    ): Converter<ResponseBody, Any>?
}