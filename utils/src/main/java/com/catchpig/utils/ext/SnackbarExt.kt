package com.catchpig.utils.ext

import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.view.updateMargins
import com.google.android.material.snackbar.Snackbar

/**
 * 设置文字的颜色
 */
fun Snackbar.setTextColor(@ColorRes textColor: Int) {
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
fun Snackbar.setMargin(leftDp: Int, topDp: Int, rightDp: Int, bottomDp: Int) {
    val view = view as Snackbar.SnackbarLayout
    val layoutParams = view.layoutParams as FrameLayout.LayoutParams
    layoutParams.updateMargins()
    layoutParams.marginStart = view.context.dp2px(leftDp)
    layoutParams.topMargin = view.context.dp2px(topDp)
    layoutParams.marginEnd = view.context.dp2px(rightDp)
    layoutParams.bottomMargin = view.context.dp2px(bottomDp)
}