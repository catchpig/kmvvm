package com.catchpig.utils.ext

import android.os.Build
import android.widget.TextView
import androidx.annotation.ColorRes

fun TextView.setTextColorRes(@ColorRes textColor: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        setTextColor(context.getColor(textColor))
    } else {
        setTextColor(context.resources.getColor(textColor))
    }
}