package com.catchpig.mvvm.entity

/**
 * @author catchpig
 * @date 2019/10/18 0018
 */
data class StatusBarParam (
        /**
         * 隐藏状态栏
         */
        val hide:Boolean = false,
        /**
         * 状态栏注解是否可用
         */
        val enabled:Boolean = true,
        /**
         * 状态栏透明
         */
        val transparent:Boolean = false
)