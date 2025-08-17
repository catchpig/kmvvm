package com.catchpig.utils.logger

import com.catchpig.utils.DEFAULT_TAG
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.LogcatLogStrategy

class MvvmLogStrategy : FormatStrategy {
    companion object {
        private const val MAX_SIZE = 200
    }

    private val methodCount: Int
    private val tag: String
    private val logStrategy by lazy {
        LogcatLogStrategy()
    }

    private constructor(builder: Builder) {
        methodCount = builder.methodCount
        tag = builder.tag
    }

    override fun log(priority: Int, onceOnlyTag: String?, message: String) {
        val tag = formatTag(onceOnlyTag!!)
        logStackTrace(priority, tag, methodCount)
        logChunk(priority, tag, message)
    }

    private fun logStackTrace(logType: Int, tag: String, methodCount: Int) {
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

    private fun formatTag(tag: String?): String {
        if (tag != null && tag.isNotEmpty()) {
            return this.tag + "-" + tag
        }
        return this.tag
    }

    private fun getSimpleClassName(name: String): String {
        val lastIndex = name.lastIndexOf(".")
        return name.substring(lastIndex + 1)
    }

    private fun logChunk(priority: Int, tag: String, chunk: String) {
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
                logStrategy.log(priority, tag, chunk.substring(start, end))
            }
        } else {
            logStrategy.log(priority, tag, chunk)
        }
    }

    class Builder {
        internal var methodCount = 0
        internal var tag = DEFAULT_TAG
        fun methodCount(methodCount: Int): Builder {
            this.methodCount = methodCount
            return this
        }

        fun tag(tag: String): Builder {
            this.tag = tag
            return this
        }

        fun build(): MvvmLogStrategy {
            val strategy = MvvmLogStrategy(this)
            return strategy
        }
    }

}