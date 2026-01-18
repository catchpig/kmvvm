package com.catchpig.mvvm.manager

import android.app.Activity
import android.view.Display
import com.catchpig.utils.ext.getDisplayId
import com.catchpig.utils.ext.logi

object ActivityManager {
    private const val TAG = "ActivityManager"
    private val displayActivities = mutableMapOf<Int, MutableList<Activity>>()
    private val displayVisibleActivityCount = mutableMapOf<Int, Int>()
    private val displayAppForeground = mutableMapOf<Int, Boolean>()
    private val displayAppStatusListener = mutableMapOf<Int, MutableList<OnAppStatusListener>>()

    fun addAppStatusListener(listener: OnAppStatusListener) {
        addAppStatusListener(Display.DEFAULT_DISPLAY, listener)
    }

    fun addAppStatusListener(displayId: Int, listener: OnAppStatusListener) {
        val appStatusListeners = getAppStatusListeners(displayId)
        if (appStatusListeners.contains(listener)) {
            "addAppStatusListener->listener:$listener has already been added".logi(TAG)
            return
        }
        appStatusListeners.add(listener)
    }

    fun removeAppStatusListener(listener: OnAppStatusListener) {
        removeAppStatusListener(Display.DEFAULT_DISPLAY, listener)
    }

    fun removeAppStatusListener(displayId: Int, listener: OnAppStatusListener) {
        val appStatusListeners = getAppStatusListeners(displayId)
        appStatusListeners.remove(listener)
    }

    private fun getAppStatusListeners(displayId: Int): MutableList<OnAppStatusListener> {
        if (displayAppStatusListener.containsKey(displayId)) {
            return displayAppStatusListener[displayId]!!
        }
        val mutableList = mutableListOf<OnAppStatusListener>()
        displayAppStatusListener[displayId] = mutableList
        return mutableList
    }

    /**
     *
     */
    fun addVisibleActivity(activity: Activity) {
        val displayId = activity.getDisplayId()
        var visibleActivityCount = getVisibleActivityCount(displayId)
        visibleActivityCount++
        displayVisibleActivityCount[displayId] = visibleActivityCount
        notifyAppStatus(displayId)
    }

    fun removeVisibleActivity(activity: Activity) {
        val displayId = activity.getDisplayId()
        var visibleActivityCount = getVisibleActivityCount(displayId)
        visibleActivityCount--
        displayVisibleActivityCount[displayId] = visibleActivityCount
        notifyAppStatus(displayId)
    }

    private fun getVisibleActivityCount(displayId: Int): Int {
        return displayVisibleActivityCount[displayId] ?: 0
    }

    /**
     * 应用是否在前台
     */
    fun isForeground(displayId: Int): Boolean {
        val visibleActivityCount = getVisibleActivityCount(displayId)
        return visibleActivityCount > 0
    }

    /**
     * 通知应用前后台状态
     */
    private fun notifyAppStatus(displayId: Int) {
        val currentAppForeground = isForeground(displayId)
        val lastAppForeground = displayAppForeground[displayId] ?: false
        if (currentAppForeground == lastAppForeground) {
            return
        }
        "notifyAppStatus->displayId:${displayId},currentAppForeground:$currentAppForeground,lastAppForeground:$lastAppForeground".logi(
            TAG
        )
        displayAppForeground[displayId] = currentAppForeground
        val appStatusListeners = getAppStatusListeners(displayId)
        appStatusListeners.forEach {
            if (currentAppForeground) {
                it.onForeground(displayId)
            } else {
                it.onBackground(displayId)
            }
        }
    }

    private fun getActivities(displayId: Int): MutableList<Activity> {
        if (displayActivities.containsKey(displayId)) {
            return displayActivities[displayId]!!

        }
        val mutableList = mutableListOf<Activity>()
        displayActivities[displayId] = mutableList
        return mutableList
    }

    /**
     * 添加activity
     */
    fun addActivity(activity: Activity) {
        val activities = getActivities(activity.getDisplayId())
        activities.add(activity)
    }

    /**
     * 删除activity
     */
    fun removeActivity(activity: Activity) {
        val activities = getActivities(activity.getDisplayId())
        activities.remove(activity)
    }

    /**
     * 获取最顶层的activity
     */
    fun getTopActivity(): Activity {
        return getTopActivity(Display.DEFAULT_DISPLAY)
    }

    /**
     * 获取最顶层的activity
     * @param displayId
     */
    fun getTopActivity(displayId: Int): Activity {
        val activities = getActivities(displayId)
        return activities.last()
    }

    /**
     * 获取第一个Activity
     */
    fun getFirstActivity(): Activity {
        return getFirstActivity(Display.DEFAULT_DISPLAY)
    }

    /**
     * 获取第一个Activity
     * @param displayId
     */
    fun getFirstActivity(displayId: Int): Activity {
        val activities = getActivities(displayId)
        return activities.first()
    }

    /**
     * 删除除最上层之外的所有activity
     */
    fun finishAllActivitiesExceptTop() {
        finishAllActivitiesExceptTop(Display.DEFAULT_DISPLAY)
    }

    /**
     * 删除除最上层之外的所有activity
     * @param displayId
     */
    fun finishAllActivitiesExceptTop(displayId: Int) {
        val topActivity = getTopActivity(displayId)
        val activities = getActivities(displayId)
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
        for (displayId in displayActivities.keys) {
            finishAllActivities(displayId)
        }
    }

    /**
     * 删除所有的activity
     * @param displayId
     */
    fun finishAllActivities(displayId: Int) {
        val activities = getActivities(displayId)
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
        fun onForeground(displayId: Int)

        /**
         * 后台
         */
        fun onBackground(displayId: Int)
    }
}