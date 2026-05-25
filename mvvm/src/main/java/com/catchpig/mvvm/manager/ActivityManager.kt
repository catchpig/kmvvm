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

    /**
     * 添加默认屏幕的应用前后台状态监听器
     * @param listener 状态监听器
     */
    fun addAppStatusListener(listener: OnAppStatusListener) {
        addAppStatusListener(Display.DEFAULT_DISPLAY, listener)
    }

    /**
     * 添加指定屏幕的应用前后台状态监听器，重复添加会忽略
     * @param displayId 屏幕 ID
     * @param listener 状态监听器
     */
    fun addAppStatusListener(displayId: Int, listener: OnAppStatusListener) {
        val appStatusListeners = getAppStatusListeners(displayId)
        if (appStatusListeners.contains(listener)) {
            "addAppStatusListener->listener:$listener has already been added".logi(TAG)
            return
        }
        appStatusListeners.add(listener)
    }

    /**
     * 移除默认屏幕的应用前后台状态监听器
     * @param listener 状态监听器
     */
    fun removeAppStatusListener(listener: OnAppStatusListener) {
        removeAppStatusListener(Display.DEFAULT_DISPLAY, listener)
    }

    /**
     * 移除指定屏幕的应用前后台状态监听器
     * @param displayId 屏幕 ID
     * @param listener 状态监听器
     */
    fun removeAppStatusListener(displayId: Int, listener: OnAppStatusListener) {
        val appStatusListeners = getAppStatusListeners(displayId)
        appStatusListeners.remove(listener)
    }

    private fun getAppStatusListeners(displayId: Int): MutableList<OnAppStatusListener> =
        displayAppStatusListener.getOrPut(displayId) { mutableListOf() }

    /**
     * 记录 Activity 进入可见状态，用于统计前台 Activity 数量
     * @param activity 进入前台的 Activity
     */
    fun addVisibleActivity(activity: Activity) {
        val displayId = activity.getDisplayId()
        var visibleActivityCount = getVisibleActivityCount(displayId)
        visibleActivityCount++
        displayVisibleActivityCount[displayId] = visibleActivityCount
        notifyAppStatus(displayId)
    }

    /**
     * 记录 Activity 退出可见状态，用于统计前台 Activity 数量
     * @param activity 退出前台的 Activity
     */
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
     * 判断指定屏幕的应用是否在前台
     * @param displayId 屏幕 ID
     * @return true 表示有可见 Activity，应用在前台；false 表示应用在后台
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

    private fun getActivities(displayId: Int): MutableList<Activity> =
        displayActivities.getOrPut(displayId) { mutableListOf() }

    /**
     * 将 Activity 加入管理栈，在 onCreate 时调用
     * @param activity 创建的 Activity
     */
    fun addActivity(activity: Activity) {
        val activities = getActivities(activity.getDisplayId())
        activities.add(activity)
    }

    /**
     * 将 Activity 从管理栈中移除，在 onDestroy 时调用
     * @param activity 销毁的 Activity
     */
    fun removeActivity(activity: Activity) {
        val activities = getActivities(activity.getDisplayId())
        activities.remove(activity)
    }

    /**
     * 获取默认屏幕栈顶 Activity
     * @return 栈顶 Activity，栈为空时返回 null
     */
    fun getTopActivity(): Activity? = getTopActivity(Display.DEFAULT_DISPLAY)

    /**
     * 获取指定屏幕栈顶 Activity
     * @param displayId 屏幕 ID
     * @return 栈顶 Activity，栈为空时返回 null
     */
    fun getTopActivity(displayId: Int): Activity? = getActivities(displayId).lastOrNull()

    /**
     * 获取默认屏幕最早启动的 Activity（栈底）
     * @return 栈底 Activity，栈为空时返回 null
     */
    fun getFirstActivity(): Activity? = getFirstActivity(Display.DEFAULT_DISPLAY)

    /**
     * 获取指定屏幕最早启动的 Activity（栈底）
     * @param displayId 屏幕 ID
     * @return 栈底 Activity，栈为空时返回 null
     */
    fun getFirstActivity(displayId: Int): Activity? = getActivities(displayId).firstOrNull()

    /**
     * 关闭默认屏幕中除栈顶以外的所有 Activity
     */
    fun finishAllActivitiesExceptTop() {
        finishAllActivitiesExceptTop(Display.DEFAULT_DISPLAY)
    }

    /**
     * 关闭指定屏幕中除栈顶以外的所有 Activity
     * @param displayId 屏幕 ID
     */
    fun finishAllActivitiesExceptTop(displayId: Int) {
        val topActivity = getTopActivity(displayId) ?: return
        getActivities(displayId).toList().forEach { activity ->
            if (activity != topActivity) {
                activity.finish()
            }
        }
    }

    /**
     * 关闭所有屏幕中的全部 Activity
     */
    fun finishAllActivities() {
        for (displayId in displayActivities.keys) {
            finishAllActivities(displayId)
        }
    }

    /**
     * 关闭指定屏幕中的全部 Activity，并同步清空管理栈
     * @param displayId 屏幕 ID
     */
    fun finishAllActivities(displayId: Int) {
        val activities = getActivities(displayId)
        val snapshot = activities.toList()
        activities.clear()
        snapshot.forEach { it.finish() }
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