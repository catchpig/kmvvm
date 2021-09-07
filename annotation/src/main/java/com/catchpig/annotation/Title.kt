package com.catchpig.annotation

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * 标题栏
 * @author catchpig
 * @date 2019/8/19 00:19
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Title(
        /**
         * 标题内容
         */
        @StringRes val value: Int,
        /**
         * 标题背景色
         */
        @ColorRes val backgroundColor: Int = -1,
        /**
         * 标题文字颜色
         */
        @ColorRes val textColor: Int = -1,
        /**
         * 返回按钮图标
         */
        @DrawableRes val backIcon: Int = -1
)