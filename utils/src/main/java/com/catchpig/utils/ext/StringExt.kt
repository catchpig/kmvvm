package com.catchpig.utils.ext

import java.net.URL
import java.util.regex.Pattern

/**
 * 判断不为null
 */
fun CharSequence?.isNotNull(): Boolean {
    this?.let {
        return true
    }
    return false
}

/**
 * 判断字符串是否为纯数字
 */
fun CharSequence?.isNumber(): Boolean {
    if (this == null) {
        return false
    }
    val pattern = Pattern.compile("[0-9]*")
    val matcher = pattern.matcher(this)
    return matcher.matches()
}


/**
 * 网络地址对否可连接
 * 此函数必须在子线程中执行
 * @receiver String
 * @return Boolean
 */
fun String.isConnected(): Boolean {
    val url = URL(this)
    try {
        url.openConnection().connect()
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }
    return true
}

