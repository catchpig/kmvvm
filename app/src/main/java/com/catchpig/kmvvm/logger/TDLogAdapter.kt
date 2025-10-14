package com.catchpig.kmvvm.logger

import com.catchpig.utils.logger.adapter.LogAdapter
import com.tencent.tddiag.logger.TDLog

class TDLogAdapter : LogAdapter() {
    override fun logi(tag: String, message: String) {
        TDLog.i(tag, message)
    }

    override fun loge(tag: String, message: String) {
        TDLog.e(tag, message)
    }

    override fun logw(tag: String, message: String) {
        TDLog.w(tag, message)
    }

    override fun logd(tag: String, message: String) {
        TDLog.d(tag, message)
    }

    override fun logv(tag: String, message: String) {
        TDLog.v(tag, message)
    }
}