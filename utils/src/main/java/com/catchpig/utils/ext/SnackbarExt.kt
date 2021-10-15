package com.catchpig.utils.ext

import android.graphics.Rect
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.google.android.material.snackbar.Snackbar

/**
 * 设置文字的颜色
 */
fun Snackbar.setTextColorRes(@ColorRes textColor: Int) {
    view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        .setTextColorRes(textColor)
}

/**
 * 设置背景颜色
 */
fun Snackbar.setBackgroundResource(@DrawableRes res: Int) {
    view.setBackgroundResource(res)
}

/**
 * 设置外边框
 */
fun Snackbar.setMargin(startDp: Int, topDp: Int, endDp: Int, bottomDp: Int) {
    val cls = this::class.java.superclass
    val field = cls.getDeclaredField("originalMargins")
    field.isAccessible = true
    val marginStart = context.dp2px(startDp)
    val topMargin = context.dp2px(topDp)
    val marginEnd = context.dp2px(endDp)
    val bottomMargin = context.dp2px(bottomDp)
    field.set(this, Rect(marginStart, topMargin, marginEnd, bottomMargin))
}