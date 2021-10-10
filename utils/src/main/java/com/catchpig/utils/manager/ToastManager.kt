package com.catchpig.utils.manager

import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import com.catchpig.utils.R

object ToastManager {
    private var toast: Toast? = null
    private lateinit var content: AppCompatTextView

    private fun init(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            toast = Toast(context)
            toast?.let {
                val view = LayoutInflater.from(context).inflate(R.layout.layout_toast, null)
                it.view = view
                content = view.findViewById(R.id.content)
            }
        }
    }

    fun show(context: Context, content: String, duration: Int) {
        if (toast != null) {
            toast!!.cancel()
        }
        init(context)
        this.content.text = content
        toast?.let {
            it.duration = duration
            it.setGravity(Gravity.CENTER, 0, 0)
            it.show()
        }
    }
}