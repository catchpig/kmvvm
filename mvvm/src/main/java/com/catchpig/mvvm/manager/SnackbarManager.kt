package com.catchpig.mvvm.manager

import android.view.Gravity
import android.view.View
import androidx.annotation.StringRes
import com.catchpig.mvvm.R
import com.catchpig.utils.ext.*
import com.google.android.material.snackbar.Snackbar

object SnackbarManager {

    fun show(view: View, text: CharSequence, gravity: Int = Gravity.BOTTOM) {
        val snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG)
        snackbar.setBackgroundResource(R.drawable.snackbar_bg)
        snackbar.setTextColorRes(R.color.color_black)
        if (gravity == Gravity.BOTTOM) {
            val parentView = snackbar.targetParent()
            val bottom = 20 + view.context.px2dp((parentView.bottom - view.bottom))
            snackbar.setMargin(20, 0, 20, bottom)
        } else {
            snackbar.setGravity(gravity)
            snackbar.setMargin(20, 0, 20, 0)
        }
        snackbar.show()
    }

    fun show(view: View, @StringRes textRes: Int, gravity: Int = Gravity.BOTTOM) {
        val snackbar = Snackbar.make(view, textRes, Snackbar.LENGTH_LONG)
        snackbar.setBackgroundResource(R.drawable.snackbar_bg)
        snackbar.setTextColorRes(R.color.color_black)
        if (gravity == Gravity.BOTTOM) {
            val parentView = snackbar.targetParent()
            val bottom = 20 + view.context.px2dp((parentView.bottom - view.bottom))
            snackbar.setMargin(20, 0, 20, bottom)
        } else {
            snackbar.setGravity(gravity)
            snackbar.setMargin(20, 0, 20, 0)
        }
        snackbar.show()
    }
}