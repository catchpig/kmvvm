package com.catchpig.mvvm.network.converter

import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer
import retrofit2.Converter
import java.lang.reflect.Type

class SerializationRequestBodyConverter(
        private val type: Type
) : Converter<Any, RequestBody> {
    companion object {
        private val MEDIA_TYPE = "application/json; charset=UTF-8".toMediaType()
    }

    override fun convert(value: Any): RequestBody? {
        val requestBodyString = Json.encodeToString(serializer(type), value)
        val buffer = Buffer()
        buffer.writeString(requestBodyString, Charsets.UTF_8)
        return buffer.readByteString().toRequestBody(MEDIA_TYPE)
    }
}