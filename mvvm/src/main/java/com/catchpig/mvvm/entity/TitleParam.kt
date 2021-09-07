package com.catchpig.mvvm.entity

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * @author catchpig
 * @date 2019/10/17 00:17
 */
data class TitleParam(
        /**
         * 标题内容
         */
        @StringRes val value:Int,
        /**
         * 标题背景色
         */
        @ColorRes val backgroundColor:Int = -1,
        /**
         * 标题文字颜色
         */
        @ColorRes val textColor:Int = -1,
        /**
         * 返回按钮图标
         */
        @DrawableRes val backIcon:Int = -1
)