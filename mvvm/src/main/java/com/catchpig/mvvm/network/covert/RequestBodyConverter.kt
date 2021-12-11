package com.catchpig.mvvm.network.covert

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer
import retrofit2.Converter
import java.io.OutputStreamWriter

class RequestBodyConverter<T>(
    private val typeAdapter: TypeAdapter<T>,
    private val gson: Gson
) : Converter<T, RequestBody> {
    companion object {
        private val MEDIA_TYPE = "application/json; charset=UTF-8".toMediaType()
    }

    override fun convert(value: T): RequestBody? {
        val buffer = Buffer()
        val writer = OutputStreamWriter(buffer.outputStream(), Charsets.UTF_8)
        val jsonWriter = gson.newJsonWriter(writer)
        typeAdapter.write(jsonWriter, value)
        jsonWriter.close()
        return buffer.readByteString().toRequestBody(MEDIA_TYPE)
    }
}