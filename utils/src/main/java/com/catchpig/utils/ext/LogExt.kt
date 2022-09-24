package com.catchpig.utils.ext

import com.catchpig.utils.LogUtils

/**
 *
 * @author TLi2
 **/
fun String.logv(tag: String) = LogUtils.getInstance().vExt(tag, this)
fun String.logd(tag: String) = LogUtils.getInstance().dExt(tag, this)
fun String.logi(tag: String) = LogUtils.getInstance().iExt(tag, this)
fun String.logw(tag: String) = LogUtils.getInstance().wExt(tag, this)
fun String.loge(tag: String) = LogUtils.getInstance().eExt(tag, this)

