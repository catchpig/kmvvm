package com.catchpig.mvvm.network.converter

import com.catchpig.mvvm.network.data.BaseResponseData
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import okhttp3.ResponseBody
import retrofit2.Converter
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.reflect.KClass

open abstract class BaseResponseBodyConverter<T> : Converter<ResponseBody, T> {
    companion object {
        private const val LIST_EMPTY = "[]"
        private const val MAP_EMPTY = "{}"
        private const val STRING_EMPTY = "\"\""
        private const val NUMBER_ZERO = "0"
    }
    lateinit var typeAdapter: TypeAdapter<out T>
    lateinit var responseType: Type
    lateinit var gson: Gson

    abstract fun getResultClass(): KClass<out BaseResponseData<*>>

    override fun convert(value: ResponseBody): T? {
        value.use { value ->
            val valueString = value.string()
            val result: BaseResponseData<*> = gson.fromJson(valueString, getResultClass().java)
            when (result.getErrorCode()) {
                result.isSuccess() -> {
                    val data = result.data()
                    return if (data == null) {
                        typeAdapter.fromJson(checkType(responseType))
                    } else {
                        typeAdapter.fromJson(gson.toJson(data))
                    }
                }
                else -> throw handlerErrorCode(result.getErrorCode(), result.getErrorMessage())
            }
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