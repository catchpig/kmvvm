package com.catchpig.mvvm.config

import com.google.gson.GsonBuilder

/**
 * 配置
 * @author catchpig
 * @date 2019/8/18 0018
 */
object Config {
    const val TIME_OUT = 5000L
    //时间格式化规则
    const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
    //没有赋值
    const val NO_ASSIGNMENT = -1

    val gson = GsonBuilder().setDateFormat(DATE_FORMAT).create()
}