package com.catchpig.utils.ext

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"

/**
 * 毫秒数转化为时间字符串
 * @receiver Long
 * @param format String
 * @return String
 */
fun Long.format(format: String = DATE_FORMAT): String {
    return Date(this).format(format)
}

/**
 * 字符串转化为时间
 */
fun String.date(format: String = DATE_FORMAT): Date {
    val dateFormat = SimpleDateFormat(format)
    return dateFormat.parse(this)!!
}

/**
 * 时间转化为字符串
 * @param format 转化格式
 * @return
 */
fun Date.format(format: String = DATE_FORMAT): String {
    val dateFormat = SimpleDateFormat(format)
    return dateFormat.format(this)
}

fun Date.calendar(): Calendar {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar
}

/**
 * 获取年
 */
fun Date.year(): Int {
    return calendar().year()
}

/**
 * 获取月份
 */
fun Date.month(): Int {
    return calendar().month()
}

/**
 * 获取月份的第几天
 */
fun Date.dayOfMonth(): Int {
    return calendar().dayOfMonth()
}

/**
 * 获取小时
 */
fun Date.hour(): Int {
    return calendar().hour()
}

/**
 * 获取分钟数
 */
fun Date.minute(): Int {
    return calendar().minute()
}

/**
 * 获取秒数
 */
fun Date.second(): Int {
    return calendar().second()
}