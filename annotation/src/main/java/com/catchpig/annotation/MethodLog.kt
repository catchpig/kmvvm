package com.catchpig.annotation

import com.catchpig.annotation.enums.LEVEL

/**
 * 打印方法(构造方法)和参数值日志
 * @author catchpig
 * @date 2020/11/9 11:26
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION,AnnotationTarget.CONSTRUCTOR)
annotation class MethodLog (
    /**
     * 日志等级
     */
    val value: LEVEL = LEVEL.D
)