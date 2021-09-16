package com.catchpig.mvvm.ext

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.catchpig.mvvm.R
import com.catchpig.utils.ext.logd

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
 * 获取标题栏的高度
 */
fun Context.getTitleHeight(): Float {
    var typedValue = TypedValue()
    theme.resolveAttribute(R.attr.title_bar_height, typedValue, true)
    val height = typedValue.getDimension(resources.displayMetrics)
    if (height <= 0) {
        throw IllegalAccessException("请主题中设置标题高度(属性名称:title_bar_height),切高度必须大于0")
    }
    return height
}

/**
 * 获取标题栏背景色
 */
@ColorRes
fun Context.getTitleBackground(): Int {
    var typedValue = TypedValue()
    theme.resolveAttribute(R.attr.title_bar_background, typedValue, true)
    if (typedValue.resourceId == Resources.ID_NULL) {
        throw IllegalAccessException("请主题中设置标题背景(属性名称:title_bar_background->@ColorRes)")
    }
    return typedValue.resourceId
}

/**
 * 获取返回按钮图标
 */
@DrawableRes
fun Context.getTitleBackIcon(): Int {
    var typedValue = TypedValue()
    theme.resolveAttribute(R.attr.title_bar_back_icon, typedValue, true)
    if (typedValue.resourceId == Resources.ID_NULL) {
        throw IllegalAccessException("请主题中设置返回按钮图标(属性名称:title_bar_back_icon -> @DrawableRes)")
    }
    return typedValue.resourceId
}

/**
 * 获取标题文字颜色
 */
@ColorInt
fun Context.getTitleTextColor(): Int {
    var typedValue = TypedValue()
    theme.resolveAttribute(R.attr.title_bar_text_color, typedValue, true)
    if (typedValue.resourceId == Resources.ID_NULL) {
        throw IllegalAccessException("请主题中设置标题文字颜色(属性名称:title_bar_text_color -> @ColorRes)")
    }
    return typedValue.data
}

/**
 * 获取全局loading的颜色
 */
@ColorRes
fun Context.getLoadingColor(): Int {
    var typedValue = TypedValue()
    theme.resolveAttribute(R.attr.loading_view_color, typedValue, true)
    if (typedValue.resourceId == Resources.ID_NULL) {
        throw IllegalAccessException("请主题中设置Loading动画颜色(属性名称:loading_view_color -> @ColorRes)")
    }
    return typedValue.resourceId
}

/**
 * 获取全局loading的背景颜色
 */
@ColorRes
fun Context.getLoadingViewBackground(): Int {
    var typedValue = TypedValue()
    theme.resolveAttribute(R.attr.loading_view_background, typedValue, true)
    if (typedValue.resourceId == Resources.ID_NULL) {
        throw IllegalAccessException("请主题中设置Loading背景颜色(属性名称:loading_view_background -> @ColorRes)")
    }
    return typedValue.resourceId
}

/**
 * 是否展示标题栏下方的线
 */
fun Context.showTitleLine(): Boolean {
    var attrs = intArrayOf(R.attr.title_bar_show_line)
    val typedArray = theme.obtainStyledAttributes(attrs)
    return typedArray.getBoolean(0, false)
}

/**
 * 获取标题下方的颜色
 */
@ColorRes
fun Context.getTitleLineColor(): Int {
    var typedValue = TypedValue()
    theme.resolveAttribute(R.attr.title_bar_line_color, typedValue, true)
    if (typedValue.resourceId == Resources.ID_NULL) {
        "主题中未设置标题栏下方的颜色,属性名称为:loading_view_background".logd("ContextExt")
    }
    return typedValue.resourceId
}

/**
 * 获取列表的空页面
 */
@LayoutRes
fun Context.getEmptyLayout(): Int {
    var typedValue = TypedValue()
    theme.resolveAttribute(R.attr.recycle_view_empty_layout, typedValue, true)
    if (typedValue.resourceId == Resources.ID_NULL) {
        "主题中未设置列表空页面,属性名称为:recycle_view_empty_layout".logd("ContextExt")
    }
    return typedValue.resourceId
}

