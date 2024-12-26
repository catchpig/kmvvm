package com.catchpig.utils.ext

import android.os.HandlerThread
import com.catchpig.utils.logger.MvvmDishLogStrategy
import com.orhanobut.logger.CsvFormatStrategy
import com.orhanobut.logger.DiskLogAdapter
import com.orhanobut.logger.Logger

fun initLogger(path: String, tag: String) {
    val handlerThread = HandlerThread("mvvm-log")
    handlerThread.start()
    val handler = MvvmDishLogStrategy.WriteHandler(handlerThread.looper, path)
    val csvFormatStrategy =
        CsvFormatStrategy.newBuilder().logStrategy(MvvmDishLogStrategy(handler)).tag(tag).build()
    Logger.addLogAdapter(DiskLogAdapter(csvFormatStrategy))
}

/**
 *
 * @author TLi2
 **/
fun String.logv(tag: String) = Logger.v(tag, this)
fun String.logd(tag: String) = Logger.d(tag, this)
fun String.logi(tag: String) = Logger.i(tag, this)
fun String.logw(tag: String) = Logger.w(tag, this)
fun String.loge(tag: String) = Logger.e(tag, this)

