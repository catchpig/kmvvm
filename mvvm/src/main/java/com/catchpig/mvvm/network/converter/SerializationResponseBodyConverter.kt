package com.catchpig.mvvm.network.converter

import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import okhttp3.ResponseBody
import retrofit2.Converter
import java.lang.reflect.Type

class SerializationResponseBodyConverter : Converter<ResponseBody, Any> {
    lateinit var type: Type
    override fun convert(value: ResponseBody): Any? {
        return Json {
            ignoreUnknownKeys = true
        }.decodeFromString(serializer(type), value.string())
    }
}