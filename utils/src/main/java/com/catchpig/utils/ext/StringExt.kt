package com.catchpig.utils.ext

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
    var pattern = Pattern.compile("[0-9]*")
    val matcher = pattern.matcher(this)
    return matcher.matches()
}

