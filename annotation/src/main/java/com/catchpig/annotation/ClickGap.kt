package com.catchpig.annotation

/**
 * 防止重复点击注解
 * @author catchpig
 * @date 2019/9/26 00:26
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class ClickGap(
        /**
         * 重复点击间隔时间
         */
        val value: Long = 800
)