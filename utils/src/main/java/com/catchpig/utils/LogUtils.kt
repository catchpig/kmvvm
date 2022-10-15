package com.catchpig.utils

import android.content.Context
import android.util.Log
import com.catchpig.utils.enums.LEVEL

class LogUtils {
    companion object {
        private const val INDEX_EXT = 6
        private const val SEPARATOR = "*";
        fun getInstance(): LogUtils {
            return LogUtilsHolder.holder
        }
    }

    private object LogUtilsHolder {
        val holder = LogUtils()
    }

    private var prefixTag: String = "kotlin-mvvm"
    private var showLineNumber = false

    /**
     * 初始化
     * @param context Context
     */
    fun init(context: Context) {
        prefixTag = context.packageName
        this.showLineNumber = showLineNumber
    }

    /**
     * 是否显示代码行数
     * @param showLineNumber Boolean
     */
    fun showLineNumber(showLineNumber: Boolean) {
        this.showLineNumber = showLineNumber
    }

    fun v(tag: String, msg: String) {
        log(LEVEL.V, tag, msg)
    }

    /**
     * 给扩展方法打印日志
     * @param tag String
     * @param msg String
     */
    internal fun vExt(tag: String, msg: String) {
        log(LEVEL.V, tag, msg)
    }

    fun i(tag: String, msg: String) {
        log(LEVEL.I, tag, msg)
    }

    /**
     * 给扩展方法打印日志
     * @param tag String
     * @param msg String
     */
    internal fun iExt(tag: String, msg: String) {
        log(LEVEL.I, tag, msg)
    }

    fun d(tag: String, msg: String) {
        log(LEVEL.D, tag, msg)
    }

    /**
     * 给扩展方法打印日志
     * @param tag String
     * @param msg String
     */
    internal fun dExt(tag: String, msg: String) {
        log(LEVEL.D, tag, msg)
    }

    fun w(tag: String, msg: String) {
        log(LEVEL.W, tag, msg)
    }

    /**
     * 给扩展方法打印日志
     * @param tag String
     * @param msg String
     */
    internal fun wExt(tag: String, msg: String) {
        log(LEVEL.W, tag, msg)
    }

    fun e(tag: String, msg: String) {
        log(LEVEL.D, tag, msg)
    }

    /**
     * 给扩展方法打印日志
     * @param tag String
     * @param msg String
     */
    internal fun eExt(tag: String, msg: String) {
        log(LEVEL.D, tag, msg)
    }

    private fun log(level: LEVEL, tag: String, message: String) {
        if (showLineNumber) {
            var newMessage = "$tag:$message"
            val messagelen = newMessage.length
            var lineNumber = getLineNumber(INDEX_EXT)
            val lineNumberLen = lineNumber.length
            var len = messagelen.coerceAtLeast(lineNumberLen) + 3
            var startEndMessage = ""
            for (i in 0..len) {
                startEndMessage += SEPARATOR
            }
            for (i in 0 until (len - lineNumberLen)) {
                lineNumber += " "
            }
            lineNumber += SEPARATOR
            for (i in 0 until (len - messagelen)) {
                newMessage += " "
            }
            newMessage += SEPARATOR
            log(level, startEndMessage)
            log(level, lineNumber)
            log(level, newMessage)
            log(level, startEndMessage)
        } else {
            log(level, "$tag:$message")
        }
    }

    private fun log(level: LEVEL, message: String) {
        when (level) {
            LEVEL.V -> Log.v(prefixTag, message)
            LEVEL.D -> Log.d(prefixTag, message)
            LEVEL.I -> Log.i(prefixTag, message)
            LEVEL.W -> Log.w(prefixTag, message)
            LEVEL.E -> Log.e(prefixTag, message)
        }
    }

    private fun getLineNumber(index: Int = 5): String {
        val elements = Thread.currentThread().stackTrace
        val element = elements[index]
        var className = element.className
        className = className.substring(className.lastIndexOf(".") + 1)
        val methodName = element.methodName
        val lineNumber = element.lineNumber
        return "$className:$methodName(line:$lineNumber)"
    }
}