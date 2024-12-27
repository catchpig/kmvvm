package com.catchpig.utils

import android.os.HandlerThread
import com.catchpig.utils.logger.MvvmDishLogStrategy
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.CsvFormatStrategy
import com.orhanobut.logger.DiskLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy

object LogUtils {
    /**
     * 初始化日志
     */
    fun init() {
        Logger.clearLogAdapters()
        val prettyFormatStrategy = PrettyFormatStrategy.newBuilder().build()
        Logger.addLogAdapter(AndroidLogAdapter(prettyFormatStrategy))
    }

    /**
     * 初始化日志
     * @param tag String
     */
    fun init(tag: String) {
        Logger.clearLogAdapters()
        val prettyFormatStrategy = PrettyFormatStrategy
            .newBuilder()
            .showThreadInfo(false)
            .tag(tag)
            .build()
        Logger.addLogAdapter(AndroidLogAdapter(prettyFormatStrategy))
    }

    /**
     * 初始化日志,并持久化日志到指定目录
     * @param tag String
     * @param diskPath String
     */
    fun init(tag: String, diskPath: String) {
        Logger.clearLogAdapters()
        val prettyFormatStrategy = PrettyFormatStrategy
            .newBuilder()
            .showThreadInfo(false)
            .tag(tag)
            .build()
        Logger.addLogAdapter(AndroidLogAdapter(prettyFormatStrategy))

        val handlerThread = HandlerThread("mvvm-log")
        handlerThread.start()
        val handler = MvvmDishLogStrategy.WriteHandler(handlerThread.looper, diskPath)
        val csvFormatStrategy = CsvFormatStrategy
            .newBuilder()
            .logStrategy(MvvmDishLogStrategy(handler))
            .tag(tag)
            .build()
        Logger.addLogAdapter(DiskLogAdapter(csvFormatStrategy))
    }
}