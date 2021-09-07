package com.catchpig.mvvm.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.catchpig.mvvm.manager.KTActivityManager

class ActivityManagerLifeCycleCallbacksImpl: Application.ActivityLifecycleCallbacks {
    override fun onActivityPaused(p0: Activity) {
    }

    override fun onActivityStarted(p0: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        KTActivityManager.removeActivity(activity)
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
    }

    override fun onActivityStopped(p0: Activity) {
    }

    override fun onActivityCreated(activity: Activity, p1: Bundle?) {
        KTActivityManager.addActivity(activity)
    }

    override fun onActivityResumed(p0: Activity) {
    }
}