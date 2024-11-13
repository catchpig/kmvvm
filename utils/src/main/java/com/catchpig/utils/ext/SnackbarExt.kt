package com.catchpig.utils.ext

import android.view.Gravity
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.updateLayoutParams
import com.catchpig.utils.R
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar(
    text: CharSequence,
    @DrawableRes background: Int,
    gravity: Int = Gravity.BOTTOM
) {
    val snackbar = Snackbar.make(this, text, Snackbar.LENGTH_LONG)
    snackbar.setBackgroundResource(background)
    snackbar.setTextColorRes(R.color.color_black)
    if (gravity == Gravity.BOTTOM) {
        val layoutParams = snackbar.view.layoutParams as MarginLayoutParams
        layoutParams.bottomMargin = dp2px(20)
        snackbar.view.layoutParams = layoutParams
    } else {
        snackbar.setGravity(gravity)
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
        val layoutParams = snackbar.view.layoutParams as MarginLayoutParams
        layoutParams.bottomMargin = dp2px(20)
        snackbar.view.layoutParams = layoutParams
    } else {
        snackbar.setGravity(gravity)
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