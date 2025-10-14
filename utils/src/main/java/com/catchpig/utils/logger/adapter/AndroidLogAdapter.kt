package com.catchpig.utils.logger.adapter

import android.util.Log

class AndroidLogAdapter() : LogAdapter() {
    override fun logi(tag: String, message: String) {
        Log.i(tag, message)
    }

    override fun logd(tag: String, message: String) {
        Log.d(tag, message)
    }

    override fun loge(tag: String, message: String) {
        Log.e(tag, message)
    }

    override fun logw(tag: String, message: String) {
        Log.w(tag, message)
    }

    override fun logv(tag: String, message: String) {
        Log.v(tag, message)
    }
}