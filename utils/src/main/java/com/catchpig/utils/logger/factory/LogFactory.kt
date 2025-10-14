package com.catchpig.utils.logger.factory

import com.catchpig.utils.logger.adapter.AndroidLogAdapter
import com.catchpig.utils.logger.adapter.LogAdapter

object LogFactory {
    private var logAdapter: LogAdapter = AndroidLogAdapter()
    private var tag: String = ""
    fun setTag(tag: String) {
        this.tag = tag
        logAdapter.setTag(tag)
    }

    fun setLogProxy(logAdapter: LogAdapter) {
        this.logAdapter = logAdapter
        setTag(tag)
    }

    fun i(subTag: String, message: String) {
        logAdapter.i(subTag, message)
    }

    fun e(subTag: String, message: String) {
        logAdapter.e(subTag, message)
    }

    fun e(subTag: String, message: String, e: Throwable? = null) {
        if (e == null) {
            logAdapter.e(subTag, message)
        } else {
            logAdapter.e(e, subTag, message)
        }
    }

    fun w(subTag: String, message: String, e: Throwable? = null) {
        if (e == null) {
            logAdapter.w(subTag, message)
        } else {
            logAdapter.w(e, subTag, message)
        }
    }

    fun d(subTag: String, message: String) {
        logAdapter.d(subTag, message)
    }

    fun dTrack(methodCount: Int, subTag: String, message: String) {
        logAdapter.dTrack(methodCount, subTag, message)
    }

    fun v(subTag: String, message: String) {
        logAdapter.v(subTag, message)
    }
}