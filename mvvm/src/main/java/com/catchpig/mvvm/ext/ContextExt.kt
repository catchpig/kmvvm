package com.catchpig.mvvm.ext

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import com.catchpig.mvvm.R
import com.catchpig.utils.ext.logd

private const val TAG = "ContextExt"
/**
 * 全局主题的配置信息
 * @author catchpig
 * @date 2019/8/19 00:19
 */
/**
 * 获取主题色
 */
@ColorRes
fun Context.getColorPrimary(): Int {
    var typedValue = TypedValue()
    theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)
    return typedValue.resourceId
}
/**
 * 获取colorRes的颜色值
 */
fun Context.getColor(@ColorRes colorRes: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        getColor(colorRes)
    } else {
        resources.getColor(colorRes)
    }
}

