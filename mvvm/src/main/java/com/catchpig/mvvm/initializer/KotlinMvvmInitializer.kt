package com.catchpig.mvvm.initializer

import android.app.Application
import android.content.Context
import androidx.startup.Initializer
import com.catchpig.mvvm.lifecycle.ActivityLifeCycleCallbacksImpl
import com.catchpig.mvvm.manager.ContextManager
import com.catchpig.mvvm.network.manager.DownloadManager
import com.catchpig.utils.LogUtils

internal class KotlinMvvmInitializer : Initializer<Boolean> {
    override fun create(context: Context): Boolean {
        val applicationContext = context!!.applicationContext
        initContext(applicationContext)
        initDownload(applicationContext)
        initLog(applicationContext)
        return true
    }

    private fun initContext(applicationContext: Context) {
        ContextManager.getInstance().init(applicationContext)
        val application = applicationContext as Application
        application.registerActivityLifecycleCallbacks(ActivityLifeCycleCallbacksImpl())
    }

    private fun initDownload(context: Context) {
        val downloadPath = "${context.externalCacheDir!!.absolutePath}/kmvvmDownload"
        DownloadManager.setDownloadPath(downloadPath)
    }

    private fun initLog(context: Context) {
        LogUtils.getInstance().init(context)
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}