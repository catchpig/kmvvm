package com.catchpig.mvvm.ext

import com.catchpig.mvvm.config.Config
import com.google.gson.Gson


/**
 * json转实体类
 */
inline fun <reified T> String.jsonToClass(gson: Gson=Config.gson):T{
    return gson.fromJson(this,T::class.java)
}

/**
 * json转字符串
 */
fun Any.jsonToString(gson: Gson=Config.gson):String{
    return gson.toJson(this)
}


