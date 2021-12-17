package com.catchpig.mvvm.network.converter

import com.catchpig.annotation.interfaces.SerializationConverter
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer
import java.lang.reflect.Type

class SerializationRequestBodyConverter(
        private val type: Type
) : SerializationConverter<Any, RequestBody> {
    companion object {
        private val MEDIA_TYPE = "application/json; charset=UTF-8".toMediaType()
    }

    override var json: Json = Json
    override fun convert(value: Any): RequestBody? {
        val requestBodyString = json.encodeToString(serializer(type), value)
        val buffer = Buffer()
        buffer.writeString(requestBodyString, Charsets.UTF_8)
        return buffer.readByteString().toRequestBody(MEDIA_TYPE)
    }
}