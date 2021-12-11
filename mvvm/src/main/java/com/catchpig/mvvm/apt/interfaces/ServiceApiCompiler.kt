package com.catchpig.mvvm.apt.interfaces

import com.catchpig.mvvm.entity.ServiceParam
import com.catchpig.mvvm.network.covert.BaseResponseBodyConverter
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import okhttp3.ResponseBody
import retrofit2.Converter
import java.lang.reflect.Type

interface ServiceApiCompiler {
    fun getServiceParam(className: String): ServiceParam

    fun getResponseBodyConverter(
        className: String,
        typeAdapter: TypeAdapter<Any>,
        type: Type,
        gson: Gson
    ): Converter<ResponseBody, Any>?
}