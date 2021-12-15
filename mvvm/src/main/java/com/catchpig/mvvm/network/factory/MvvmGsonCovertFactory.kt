package com.catchpig.mvvm.network.factory

import com.catchpig.mvvm.apt.KotlinMvvmCompiler
import com.catchpig.mvvm.network.converter.GsonRequestBodyConverter
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class MvvmGsonCovertFactory(private val serviceClassName: String) :
    Converter.Factory() {
    companion object {
        @JvmStatic
        fun create(serviceClassName: String): MvvmGsonCovertFactory {
            return MvvmGsonCovertFactory(serviceClassName)
        }
    }

    private val gson = GsonBuilder().create()

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        val adapter: TypeAdapter<out Any> = gson.getAdapter(TypeToken.get(type))
        return GsonRequestBodyConverter(adapter, gson)
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        return KotlinMvvmCompiler.getResponseBodyConverter(serviceClassName, type)
    }
}