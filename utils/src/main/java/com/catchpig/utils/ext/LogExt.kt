package com.catchpig.utils.ext

import com.catchpig.utils.LogUtils

/**
 *
 * @author TLi2
 **/
fun String.logv(tag: String) = LogUtils.vExt(tag, this)
fun String.logd(tag: String) = LogUtils.dExt(tag, this)
fun String.logi(tag: String) = LogUtils.iExt(tag, this)
fun String.logw(tag: String) = LogUtils.wExt(tag, this)
fun String.loge(tag: String) = LogUtils.eExt(tag, this)

