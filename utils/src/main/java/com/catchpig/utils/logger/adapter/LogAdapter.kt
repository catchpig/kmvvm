package com.catchpig.utils.logger.adapter

import android.util.Log


abstract class LogAdapter() {
    companion object {
        private const val MAX_SIZE = 200
    }

    private var tag: String = ""

    fun setTag(tag: String) {
        this.tag = tag
    }

    fun i(subTag: String, message: String) {
        logChunk(Log.INFO, formatTag(subTag), message)
    }

    fun e(subTag: String, message: String) {
        logChunk(Log.ERROR, formatTag(subTag), message)
    }

    fun e(e: Throwable, subTag: String, message: String) {
        logChunk(Log.ERROR, formatTag(subTag), message)
        e.stackTrace.forEach {
            logChunk(Log.ERROR, formatTag(subTag), it.toString())
        }
    }

    fun w(subTag: String, message: String) {
        logChunk(Log.WARN, formatTag(subTag), message)
    }

    fun w(e: Throwable, subTag: String, message: String) {
        logChunk(Log.WARN, formatTag(subTag), message)
        e.stackTrace.forEach {
            logChunk(Log.WARN, formatTag(subTag), it.toString())
        }
    }

    fun d(subTag: String, message: String) {
        logChunk(Log.DEBUG, formatTag(subTag), message)
    }

    fun dTrack(methodCount: Int, subTag: String, message: String) {
        logStackTrace(Log.DEBUG, formatTag(subTag), methodCount)
        logChunk(Log.DEBUG, formatTag(subTag), message)
    }

    fun v(subTag: String, message: String) {
        logChunk(Log.VERBOSE, formatTag(subTag), message)
    }

    private fun formatTag(subTag: String): String {
        if (tag.isEmpty()) {
            return subTag
        }
        if (subTag.isNotEmpty()) {
            return "$tag-$subTag"
        }
        return tag
    }

    fun logStackTrace(logType: Int, tag: String, methodCount: Int) {
        if (methodCount <= 0) {
            return
        }
        val trace = Thread.currentThread().getStackTrace()
        for (i in 0 until methodCount) {
            val stackIndex: Int = i + 8
            if (stackIndex >= trace.size) {
                continue
            }
            val builder = StringBuilder()
            builder.append(getSimpleClassName(trace[stackIndex].className))
                .append(".")
                .append(trace[stackIndex].methodName)
                .append(" ")
                .append(" (")
                .append(trace[stackIndex].fileName)
                .append(":")
                .append(trace[stackIndex].lineNumber)
                .append(")")
            logChunk(logType, tag, builder.toString())
        }
    }

    private fun getSimpleClassName(name: String): String {
        val lastIndex = name.lastIndexOf(".")
        return name.substring(lastIndex + 1)
    }

    fun logChunk(priority: Int, tag: String, chunk: String) {
        val length = chunk.length
        if (length > MAX_SIZE) {
            val count = if (length % MAX_SIZE == 0) {
                length / MAX_SIZE
            } else {
                (length / MAX_SIZE) + 1
            }
            for (i in 0 until count) {
                val start = i * MAX_SIZE
                var end = (i + 1) * MAX_SIZE
                if (end > length) {
                    end = length
                }
                log(priority, tag, chunk.substring(start, end))
            }
        } else {
            log(priority, tag, chunk)
        }
    }

    private fun log(priority: Int, tag: String, message: String) {
        when (priority) {
            Log.INFO -> logi(tag, message)
            Log.ERROR -> loge(tag, message)
            Log.WARN -> logw(tag, message)
            Log.DEBUG -> logd(tag, message)
            Log.VERBOSE -> logv(tag, message)
            else -> {}
        }
    }

    protected abstract fun logi(tag: String, message: String)
    protected abstract fun logd(tag: String, message: String)
    protected abstract fun loge(tag: String, message: String)
    protected abstract fun logw(tag: String, message: String)
    protected abstract fun logv(tag: String, message: String)
}