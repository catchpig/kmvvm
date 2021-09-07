package com.catchpig.annotation

import com.catchpig.annotation.enums.LEVEL

/**
 * 方法和构造方法的耗时打印
 * @author catchpig
 * @date 2019/10/20 00:20
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION,AnnotationTarget.CONSTRUCTOR)
annotation class TimeLog(
        /**
         * 日志等级
         */
        val value:LEVEL = LEVEL.D
)
