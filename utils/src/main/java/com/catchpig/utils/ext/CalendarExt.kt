package com.catchpig.utils.ext

import java.util.*

/**
 * 字符串转化为时间
 */
fun String.calendar(format:String=DATE_FORMAT):Calendar{
    return date(format).calendar()
}

/**
 * 时间转化为字符串
 * @param format 转化格式
 * @return
 */
fun Calendar.format(format: String= DATE_FORMAT):String{
    return time.format(format)
}

/**
 * 获取年
 */
fun Calendar.year(): Int {
    return this.get(Calendar.YEAR)
}

/**
 * 获取月份
 */
fun Calendar.month(): Int {
    return get(Calendar.MONTH)+1
}

/**
 * 获取月份的第几天
 */
fun Calendar.dayOfMonth(): Int {
    return get(Calendar.DAY_OF_MONTH)
}

/**
 * 获取小时
 */
fun Calendar.hour(): Int {
    return get(Calendar.HOUR_OF_DAY)
}

/**
 * 获取分钟数
 */
fun Calendar.minute():Int{
    return get(Calendar.MINUTE)
}

/**
 * 获取秒数
 */
fun Calendar.second():Int{
    return get(Calendar.SECOND)
}