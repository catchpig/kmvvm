package com.catchpig.mvvm.network.converter

import com.catchpig.annotation.interfaces.SerializationConverter
import com.catchpig.mvvm.network.data.BaseResponseData
import kotlinx.serialization.KSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.serializer
import okhttp3.ResponseBody
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.reflect.KClass

open abstract class BaseResponseBodyConverter :
        SerializationConverter<ResponseBody, Any> {
    companion object {
        private const val LIST_EMPTY = "[]"
        private const val MAP_EMPTY = "{}"
        private const val STRING_EMPTY = "\"\""
        private const val NUMBER_ZERO = "0"
    }

    lateinit var type: Type

    abstract fun getResultClass(): KClass<out BaseResponseData<JsonElement>>

    override fun convert(value: ResponseBody): Any? {
        val valueString = value.string()
        val kSerializer: KSerializer<Any> =
                serializer(getResultClass().java)
        val result: BaseResponseData<JsonElement> =
                json.decodeFromString(kSerializer, valueString) as BaseResponseData<JsonElement>
        when (result.getErrorCode()) {
            result.isSuccess() -> {
                val data = result.data()
                return if (data == null) {
                    json.decodeFromString(serializer(type), checkType(type))
                } else {
                    json.decodeFromString(serializer(type), json.encodeToString(data))
                }
            }
            else -> throw handlerErrorCode(result.getErrorCode(), result.getErrorMessage())
        }
    }

    abstract fun handlerErrorCode(errorCode: String, msg: String): Exception

    private fun checkType(type: Type): String {
        return when (type) {
            is ParameterizedType -> {
                when (type.rawType) {
                    List::class.java -> {
                        LIST_EMPTY
                    }
                    else -> {
                        MAP_EMPTY
                    }
                }
            }
            String::class.java -> {
                STRING_EMPTY
            }
            Int::class.java,
            Double::class.java,
            Float::class.java,
            Long::class.java -> {
                NUMBER_ZERO
            }
            else -> {
                MAP_EMPTY
            }
        }
    }
}
