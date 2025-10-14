package com.catchpig.utils

import com.catchpig.utils.logger.adapter.LogAdapter
import com.catchpig.utils.logger.factory.LogFactory

const val DEFAULT_TAG = "KMVVM"

object LogUtils {

    /**
     * 初始化日志
     * @param tag String
     * @param methodCount Int 方法数
     */
    fun init(tag: String = DEFAULT_TAG, logAdapter: LogAdapter? = null) {
        LogFactory.setTag(tag)
        logAdapter?.let {
            LogFactory.setLogProxy(it)
        }
    }
}