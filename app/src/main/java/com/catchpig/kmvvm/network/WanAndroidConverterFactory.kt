package com.catchpig.kmvvm.network

import com.catchpig.kmvvm.exception.HttpServerException
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import okio.Buffer
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.OutputStreamWriter
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @author litao
 * date 2021/9/11
 * description:
 */
class WanAndroidConverterFactory private constructor(private var gson: Gson) : Converter.Factory() {

    companion object {
        private const val LIST_EMPTY = "[]"
        private const val MAP_EMPTY = "{}"
        private const val STRING_EMPTY = "\"\""
        private const val NUMBER_ZERO = "0"
        private val MEDIA_TYPE = "application/json; charset=UTF-8".toMediaType()
        fun create(): WanAndroidConverterFactory {
            val gson = GsonBuilder().create()
            return WanAndroidConverterFactory(gson)
        }
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        val adapter: TypeAdapter<out Any> = gson.getAdapter(TypeToken.get(type))
        return RequestBodyConverter(adapter, gson)
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        val typeAdapter = gson.getAdapter(TypeToken.get(type))
        return ResponseBodyConverter(typeAdapter, type, gson)
    }

    class ResponseBodyConverter<T>(
        private val typeAdapter: TypeAdapter<T>,
        private val responseType: Type,
        private val gson: Gson
    ) : Converter<ResponseBody, T> {
        override fun convert(value: ResponseBody): T? {
            val type = object : TypeToken<Result<T>>() {}.type
            value.use { value ->
                val result: Result<T> = gson.fromJson(value.string(), type)
                val msg = result.errorMsg
                when (val code = result.errorCode) {
                    Result.SUCCESS_CODE -> {
                        val data = result.data
                        return if (data == null) {
                            typeAdapter.fromJson(checkType(responseType))
                        } else {
                            typeAdapter.fromJson(gson.toJson(data))
                        }
                    }
                    else -> throw HttpServerException(code, msg)
                }
            }
        }

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

    class RequestBodyConverter<T>(
        private val typeAdapter: TypeAdapter<T>,
        private val gson: Gson
    ) :
        Converter<T, RequestBody> {
        override fun convert(value: T): RequestBody? {
            val buffer = Buffer()
            val writer = OutputStreamWriter(buffer.outputStream(), Charsets.UTF_8)
            val jsonWriter = gson.newJsonWriter(writer)
            typeAdapter.write(jsonWriter, value)
            jsonWriter.close()
            return buffer.readByteString().toRequestBody(MEDIA_TYPE)
        }

    }
}
