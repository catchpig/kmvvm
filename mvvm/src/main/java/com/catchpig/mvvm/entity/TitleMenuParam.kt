package com.catchpig.mvvm.entity

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * @author catchpig
 * @date 2019/10/17 00:17
 */
data class TitleMenuParam(
        /**
         * 右边第一个按钮图标
         */
        @DrawableRes val rightFirstDrawable:Int = -1,
        /**
         * 右边第一个按钮文字
         */
        @StringRes val rightFirstText:Int = -1,
        /**
         * 右边第二个按钮图标
         */
        @DrawableRes val rightSecondDrawable:Int = -1,
        /**
         * 右边第二个按钮文字
         */
        @StringRes val rightSecondText:Int = -1
)