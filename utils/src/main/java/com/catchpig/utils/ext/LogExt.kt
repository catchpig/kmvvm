package com.catchpig.utils.ext

import com.catchpig.utils.logger.factory.LogFactory

fun String.logv(tag: String) {
    LogFactory.v(tag, this)
}

fun String.logd(tag: String) {
    LogFactory.d(tag, this)
}

fun String.logdTrack(methodCount: Int, tag: String) {
    LogFactory.dTrack(methodCount, tag, this)
}


fun String.logi(tag: String) {
    LogFactory.i(tag, this)
}

fun String.logw(tag: String, t: Throwable? = null) {
    LogFactory.w(tag, this, t)
}

fun String.loge(tag: String) = {
    LogFactory.e(tag, this)
}

fun String.loge(tag: String, t: Throwable) {
    LogFactory.e(tag, this, t)
}

