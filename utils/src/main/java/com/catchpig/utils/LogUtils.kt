package com.catchpig.utils

import android.os.HandlerThread
import com.catchpig.utils.logger.MvvmDiskLogStrategy
import com.catchpig.utils.logger.MvvmLogStrategy
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.CsvFormatStrategy
import com.orhanobut.logger.DiskLogAdapter
import com.orhanobut.logger.Logger

const val DEFAULT_TAG = "KMVVM"

object LogUtils {

    /**
     * 初始化日志
     * @param tag String
     * @param methodCount Int 方法数
     */
    fun init(tag: String = DEFAULT_TAG, methodCount: Int = 0) {
        Logger.clearLogAdapters()
        val formatStrategy = MvvmLogStrategy.Builder().methodCount(methodCount).tag(tag).build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
    }

    /**
     * 初始化日志,并持久化日志到指定目录
     *
     * @param diskPath String
     * @param tag String
     * @param methodCount Int 方法数
     */
    fun init(diskPath: String, tag: String, methodCount: Int = 0) {
        Logger.clearLogAdapters()
        val formatStrategy = MvvmLogStrategy.Builder().methodCount(methodCount).tag(tag).build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))

        val handlerThread = HandlerThread("mvvm-log")
        handlerThread.start()
        val handler = MvvmDiskLogStrategy.WriteHandler(handlerThread.looper, diskPath)
        val csvFormatStrategy = CsvFormatStrategy
            .newBuilder()
            .logStrategy(MvvmDiskLogStrategy(handler))
            .tag(tag)
            .build()
        Logger.addLogAdapter(DiskLogAdapter(csvFormatStrategy))
    }
}