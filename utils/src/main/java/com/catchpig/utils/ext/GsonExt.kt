package com.catchpig.utils.ext

import com.google.gson.Gson
import com.google.gson.GsonBuilder


val defaultGson: Gson = GsonBuilder().setDateFormat(DATE_FORMAT).create()
/**
 * json转实体类
 */
inline fun <reified T> String.jsonToClass(gson: Gson=defaultGson):T{
    return gson.fromJson(this,T::class.java)
}

/**
 * json转字符串
 */
fun Any.jsonToString(gson: Gson=defaultGson):String{
    return gson.toJson(this)
}


