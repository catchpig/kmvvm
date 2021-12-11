package com.catchpig.kmvvm.network

import com.catchpig.mvvm.network.converter.BaseResponseBodyConverter
import com.catchpig.mvvm.network.data.BaseResponseData
import kotlin.reflect.KClass

class ResponseBodyConverter :
    BaseResponseBodyConverter<Any>() {
    override fun getResultClass(): KClass<out BaseResponseData<*>> {
        return Result::class
    }

    override fun handlerErrorCode(errorCode: String, msg: String): Exception {
        return NullPointerException()
    }
}