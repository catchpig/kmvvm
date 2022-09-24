package com.catchpig.utils.ext

import android.graphics.Rect
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.updateLayoutParams
import com.catchpig.utils.R
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar(text: CharSequence, @DrawableRes background: Int, gravity: Int = Gravity.BOTTOM) {
    val snackbar = Snackbar.make(this, text, Snackbar.LENGTH_LONG)
    snackbar.setBackgroundResource(background)
    snackbar.setTextColorRes(R.color.color_black)
    if (gravity == Gravity.BOTTOM) {
        val parentView = snackbar.targetParent()
        val bottom = 20 + context.px2dp((parentView.bottom - bottom))
        snackbar.setMargin(20, 0, 20, bottom)
    } else {
        snackbar.setGravity(gravity)
        snackbar.setMargin(20, 0, 20, 0)
    }
    snackbar.show()
}

fun View.showSnackBar(
    @StringRes textRes: Int,
    @DrawableRes background: Int,
    gravity: Int = Gravity.BOTTOM
) {
    val snackbar = Snackbar.make(this, textRes, Snackbar.LENGTH_LONG)
    snackbar.setBackgroundResource(background)
    snackbar.setTextColorRes(R.color.color_black)
    if (gravity == Gravity.BOTTOM) {
        val parentView = snackbar.targetParent()
        val bottom = 20 + context.px2dp((parentView.bottom - bottom))
        snackbar.setMargin(20, 0, 20, bottom)
    } else {
        snackbar.setGravity(gravity)
        snackbar.setMargin(20, 0, 20, 0)
    }
    snackbar.show()
}

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