package com.catchpig.mvvm.network.converter

import com.catchpig.annotation.interfaces.SerializationConverter
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import okhttp3.ResponseBody
import retrofit2.Converter
import java.lang.reflect.Type

class SerializationResponseBodyConverter : SerializationConverter<ResponseBody, Any> {
    lateinit var type: Type
    override var json: Json = Json {
        ignoreUnknownKeys = true
    }

    override fun convert(value: ResponseBody): Any? {
        return json.decodeFromString(serializer(type), value.string())
    }
}