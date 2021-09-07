package com.catchpig.mvvm.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.util.*

/**
 * 时间错转化时间类型
 * @author catchpig
 * @date 2019/9/27 0027
 */
class DateJsonDeserializer:JsonDeserializer<Date> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Date {
        return if (json==null) {
            Date()
        }else{
            Date(json.asJsonPrimitive.asLong)
        }
    }
}