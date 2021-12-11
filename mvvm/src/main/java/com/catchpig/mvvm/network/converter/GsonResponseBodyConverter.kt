package com.catchpig.mvvm.network.converter

import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import okhttp3.ResponseBody
import retrofit2.Converter

class GsonResponseBodyConverter : Converter<ResponseBody, Any> {
    lateinit var gson: Gson
    lateinit var typeAdapter: TypeAdapter<Any>
    override fun convert(value: ResponseBody): Any? {
        val jsonReader: JsonReader = gson.newJsonReader(value.charStream())
        return value.use {
            val result: Any = typeAdapter.read(jsonReader)
            if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw JsonIOException("JSON document was not fully consumed.")
            }
            result
        }
    }
}