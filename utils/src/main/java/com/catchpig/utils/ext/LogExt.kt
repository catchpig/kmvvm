package com.catchpig.utils.ext

import com.orhanobut.logger.Logger

fun String.logv(tag: String) = Logger.t(tag).v(this)
fun String.logd(tag: String) = Logger.t(tag).d(this)
fun String.logi(tag: String) = Logger.t(tag).i(this)
fun String.logw(tag: String) = Logger.t(tag).w(this)
fun String.loge(tag: String) = Logger.t(tag).e(this)
fun String.loge(tag: String, t: Throwable) = Logger.t(tag).e(t, this)

