package com.catchpig.utils.ext

import android.graphics.Rect
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.updateLayoutParams
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
 * 设置显示位置
 * @receiver Snackbar
 * @param gravity Int
 */
fun Snackbar.setGravity(gravity: Int) {
    view.updateLayoutParams {
        when (this) {
            is FrameLayout.LayoutParams -> {
                this.gravity = gravity
            }
            is CoordinatorLayout.LayoutParams -> {
                this.gravity = gravity
            }
        }
    }
}

fun Snackbar.targetParent(): View {
    val cls = this::class.java.superclass
    val field = cls.getDeclaredField("targetParent")
    field.isAccessible = true
    return field.get(this) as View
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