package com.catchpig.mvvm.manager

import android.app.Activity
import com.catchpig.utils.ext.logi
import java.util.LinkedList

object ActivityManager {
    private const val TAG = "ActivityManager"
    private var activities = LinkedList<Activity>()
    private var visibleActivityCount = 0
    private var isAppForeground: Boolean? = null

    private val appStatusListeners = mutableListOf<OnAppStatusListener>()

    fun addAppStatusListener(listener: OnAppStatusListener) {
        if (appStatusListeners.contains(listener)) {
            "addAppStatusListener->listener:$listener has already been added"
            return
        }
        appStatusListeners.add(listener)
    }

    fun removeAppStatusListener(listener: OnAppStatusListener) {
        appStatusListeners.remove(listener)
    }

    /**
     * t
     */
    fun addVisibleActivity() {
        visibleActivityCount++
        notifyAppStatus()
    }

    fun removeVisibleActivity() {
        visibleActivityCount--
        notifyAppStatus()
    }

    /**
     * 应用是否在前台
     */
    fun isForeground(): Boolean {
        return visibleActivityCount > 0
    }

    /**
     * 通知应用前后台状态
     */
    private fun notifyAppStatus() {
        val isForeground = isForeground()
        if (isForeground == isAppForeground) {
            return
        }
        "notifyAppStatus->isForeground:$isForeground,isAppForeground:$isAppForeground".logi(TAG)
        isAppForeground = isForeground
        appStatusListeners.forEach {
            if (isForeground) {
                it.onForeground()
            } else {
                it.onBackground()
            }
        }
    }

    /**
     * 添加activity
     */
    fun addActivity(activity: Activity) {
        activities.add(activity)
    }

    /**
     * 删除activity
     */
    fun removeActivity(activity: Activity?) {
        activities.remove(activity)
    }

    /**
     * 获取最顶层的activity
     */
    fun getTopActivity(): Activity {
        return activities.last()
    }

    /**
     * 获取第一个Activity
     */
    fun getFirstActivity(): Activity {
        return activities.first
    }

    /**
     * 删除除最上层之外的所有activity
     */
    fun finishAllActivitiesExceptTop() {
        val topActivity = getTopActivity()
        val iterator = activities.iterator()
        while (iterator.hasNext()) {
            val activity = iterator.next()
            if (activity != topActivity) {
                activity.finish()
            }
        }
    }

    /**
     * 删除所有的activity
     */
    fun finishAllActivities() {
        val iterator = activities.iterator()
        while (iterator.hasNext()) {
            val activity = iterator.next()
            activity.finish()
            iterator.remove()
        }
    }

    interface OnAppStatusListener {
        /**
         * 前台
         */
        fun onForeground()

        /**
         * 后台
         */
        fun onBackground()
    }
}