package com.catchpig.mvvm.network.factory

import com.catchpig.mvvm.apt.KotlinMvvmCompiler
import com.catchpig.mvvm.network.converter.SerializationRequestBodyConverter
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class SerializationCovertFactory(private val serviceClassName: String) :
        Converter.Factory() {
    companion object {
        @JvmStatic
        fun create(serviceClassName: String): SerializationCovertFactory {
            return SerializationCovertFactory(serviceClassName)
        }
    }

    override fun requestBodyConverter(
            type: Type,
            parameterAnnotations: Array<Annotation>,
            methodAnnotations: Array<Annotation>,
            retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        return SerializationRequestBodyConverter(type)
    }

    override fun responseBodyConverter(
            type: Type,
            annotations: Array<Annotation>,
            retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        return KotlinMvvmCompiler.getResponseBodyConverter(serviceClassName, type)
    }
}