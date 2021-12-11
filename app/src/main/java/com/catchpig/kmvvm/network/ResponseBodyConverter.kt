package com.catchpig.kmvvm.network

import com.catchpig.mvvm.network.covert.BaseResponseBodyConverter
import com.catchpig.mvvm.network.data.BaseResponseData
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import java.lang.reflect.Type
import kotlin.reflect.KClass

class ResponseBodyConverter(typeAdapter: TypeAdapter<Any>, type: Type, gson: Gson) :
    BaseResponseBodyConverter<Any>(typeAdapter, type, gson) {
    override fun getResultClass(): KClass<out BaseResponseData<*>> {
        return Result::class
    }

    override fun handlerErrorCode(errorCode: String, msg: String): Exception {
        return NullPointerException()
    }
}