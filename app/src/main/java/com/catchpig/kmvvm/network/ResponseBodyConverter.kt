package com.catchpig.kmvvm.network

import com.catchpig.mvvm.network.converter.BaseResponseBodyConverter
import com.catchpig.mvvm.network.data.BaseResponseData
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

class ResponseBodyConverter :
    BaseResponseBodyConverter() {
    override fun getResultClass(): KClass<out BaseResponseData<JsonElement>> {
        return Result::class
    }

    override fun handlerErrorCode(errorCode: String, msg: String): Exception {
        return NullPointerException()
    }
}