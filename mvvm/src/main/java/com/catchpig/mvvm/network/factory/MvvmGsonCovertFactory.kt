package com.catchpig.mvvm.network.factory

import com.catchpig.mvvm.network.covert.BaseResponseBodyConverter
import com.catchpig.mvvm.network.covert.RequestBodyConverter
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class MvvmGsonCovertFactory(private val converterClass: Class<out BaseResponseBodyConverter<*>>) :
    Converter.Factory() {
    companion object {
        @JvmStatic
        fun create(converterClass: Class<out BaseResponseBodyConverter<*>>): MvvmGsonCovertFactory {
            return MvvmGsonCovertFactory(converterClass)
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
        return RequestBodyConverter(adapter, gson)
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        val typeAdapter = gson.getAdapter(TypeToken.get(type))
        val converterConstructor = converterClass.constructors[0]
        return converterConstructor.newInstance(
            typeAdapter,
            type,
            gson
        ) as Converter<ResponseBody, *>?
    }
}