package com.catchpig.annotation

import androidx.annotation.StringRes

/**
 * 右边第二个文字按钮的点击事件
 *
 * 修饰的方法可以没有参数和一个参数,
 * 有参数的时候参数类型必须为View
 * @author catchpig
 * @date 2019/10/18 00:18

 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class OnClickSecondText(
        /**
         * 文字内容
         */
        @StringRes val value: Int
)