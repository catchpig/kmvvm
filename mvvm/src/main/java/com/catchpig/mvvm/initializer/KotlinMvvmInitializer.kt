package com.catchpig.mvvm.initializer

import android.app.Application
import android.content.Context
import androidx.startup.Initializer
import com.catchpig.mvvm.lifecycle.ActivityLifeCycleCallbacksImpl
import com.catchpig.utils.ext.logd
import com.catchpig.utils.manager.ContextManager
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

class KotlinMvvmInitializer : Initializer<Boolean> {
    companion object {
        private const val TAG = "KotlinMvvmInitializer"
    }

    override fun create(context: Context): Boolean {
        val applicationContext = context.applicationContext
        initLog()
        initContext(applicationContext)
        "create".logd(TAG)
        return true
    }

    private fun initContext(applicationContext: Context) {
        ContextManager.getInstance().init(applicationContext)
        val application = applicationContext as Application
        application.registerActivityLifecycleCallbacks(ActivityLifeCycleCallbacksImpl())
    }

    private fun initLog() {
        Logger.addLogAdapter(AndroidLogAdapter())
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}