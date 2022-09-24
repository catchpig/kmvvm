package com.catchpig.utils

import android.content.Context
import android.util.Log
import com.catchpig.utils.enums.LEVEL

class LogUtils {
    companion object {
        private const val INDEX_EXT = 5
        fun getInstance(): LogUtils {
            return LogUtilsHolder.holder
        }
    }

    private object LogUtilsHolder {
        val holder = LogUtils()
    }

    private var tag_prefix: String = "kotlin-mvvm"
    private var showLineNumber = false

    /**
     * 初始化
     * @param context Context
     * @param showLineNumber Boolean 是否展示行号
     */
    fun init(context: Context, showLineNumber: Boolean = false) {
        tag_prefix = context.packageName
        this.showLineNumber = showLineNumber
    }

    fun v(tag: String, msg: String) {
        log(LEVEL.V, getTag(tag), msg)
    }

    /**
     * 给扩展方法打印日志
     * @param tag String
     * @param msg String
     */
    internal fun vExt(tag: String, msg: String) {
        log(LEVEL.V, getTag(tag, INDEX_EXT), msg)
    }

    fun i(tag: String, msg: String) {
        log(LEVEL.I, getTag(tag), msg)
    }

    /**
     * 给扩展方法打印日志
     * @param tag String
     * @param msg String
     */
    internal fun iExt(tag: String, msg: String) {
        log(LEVEL.I, getTag(tag, INDEX_EXT), msg)
    }

    fun d(tag: String, msg: String) {
        log(LEVEL.D, getTag(tag), msg)
    }

    /**
     * 给扩展方法打印日志
     * @param tag String
     * @param msg String
     */
    internal fun dExt(tag: String, msg: String) {
        log(LEVEL.D, getTag(tag, INDEX_EXT), msg)
    }

    fun w(tag: String, msg: String) {
        log(LEVEL.W, getTag(tag), msg)
    }

    /**
     * 给扩展方法打印日志
     * @param tag String
     * @param msg String
     */
    internal fun wExt(tag: String, msg: String) {
        log(LEVEL.W, getTag(tag, INDEX_EXT), msg)
    }

    fun e(tag: String, msg: String) {
        log(LEVEL.D, getTag(tag), msg)
    }

    /**
     * 给扩展方法打印日志
     * @param tag String
     * @param msg String
     */
    internal fun eExt(tag: String, msg: String) {
        log(LEVEL.D, getTag(tag, INDEX_EXT), msg)
    }

    private fun log(level: LEVEL, tag: String, message: String) {
        when (level) {
            LEVEL.V -> Log.v(tag, message)
            LEVEL.D -> Log.d(tag, message)
            LEVEL.I -> Log.i(tag, message)
            LEVEL.W -> Log.w(tag, message)
            LEVEL.E -> Log.e(tag, message)
        }
    }

    private fun getTag(tag: String, index: Int = 5): String {
        if (showLineNumber) {
            val elements = Thread.currentThread().stackTrace
            val element = elements[index]
            val className = element.methodName
            val lineNumber = element.lineNumber
            return "${tag_prefix}:${tag}->${className}(line:${lineNumber})"
        }
        return "${tag_prefix}:${tag}"
    }
}