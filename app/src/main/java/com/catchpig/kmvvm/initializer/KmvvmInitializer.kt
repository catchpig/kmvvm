package com.catchpig.kmvvm.initializer

import android.content.Context
import androidx.startup.Initializer
import com.catchpig.kmvvm.logger.TDLogAdapter
import com.catchpig.mvvm.initializer.KotlinMvvmInitializer
import com.catchpig.utils.LogUtils
import com.catchpig.utils.ext.logi
import com.tencent.tddiag.logger.TDLog
import com.tencent.tddiag.logger.TDLogConfig

class KmvvmInitializer : Initializer<Unit> {
    companion object {
        private const val TAG = "KmvvmInitializer"
    }

    override fun create(context: Context) {
        initLog(context.applicationContext)
    }

    private fun initLog(context: Context) {
        val config = TDLogConfig.Builder(context)
            .setLogPath("${context.cacheDir!!.absolutePath}/logs")
            .setMaxAliveDay(7)
            .build()
        TDLog.initialize(context, config)
        LogUtils.init("KMVVM", TDLogAdapter())
//        LogUtils.init("KMVVM")
        "initLog".logi(TAG)
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> {
        return listOf(KotlinMvvmInitializer::class.java)
    }
}