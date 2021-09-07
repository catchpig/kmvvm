package com.catchpig.utils.ext

import android.util.Log
import com.catchpig.annotation.enums.LEVEL

const val TAG = "kotlin-mvp"
/**
 *
 * @author TLi2
 **/
fun String.logv(tag: String = TAG) = log(LEVEL.V, tag, this)
fun String.logd(tag: String = TAG) = log(LEVEL.D, tag, this)
fun String.logi(tag: String = TAG) = log(LEVEL.I, tag, this)
fun String.logw(tag: String = TAG) = log(LEVEL.W, tag, this)
fun String.loge(tag: String = TAG) = log(LEVEL.E, tag, this)

fun log(level: LEVEL, tag: String, message: String) {
    when (level) {
        LEVEL.V -> Log.v(tag, message)
        LEVEL.D -> Log.d(tag, message)
        LEVEL.I -> Log.i(tag, message)
        LEVEL.W -> Log.w(tag, message)
        LEVEL.E -> Log.e(tag, message)
    }
}