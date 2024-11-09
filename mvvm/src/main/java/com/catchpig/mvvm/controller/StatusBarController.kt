package com.catchpig.mvvm.controller

import android.app.Activity
import android.view.View
import androidx.core.view.isVisible
import com.catchpig.mvvm.R
import com.catchpig.mvvm.config.Config
import com.catchpig.mvvm.entity.StatusBarParam
import com.catchpig.mvvm.entity.TitleParam
import com.catchpig.mvvm.interfaces.IGlobalConfig
import com.catchpig.mvvm.ksp.KotlinMvvmCompiler
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.ktx.immersionBar

/**
 * 状态栏处理器
 * @author catchpig
 * @date 2019/8/18 00:18
 */
class StatusBarController(
    private val activity: Activity,
    private val title: TitleParam?,
    private val statusBar: StatusBarParam?
) {
    private val globalConfig: IGlobalConfig = KotlinMvvmCompiler.globalConfig()

    fun checkStatusBar() {
        val topView = activity.findViewById<View>(R.id.top_view)
        //状态栏注解设置为不可用
        if (statusBar != null && !statusBar.enabled) {
            topView.isVisible = false
//            activity.apply {
//                val layoutParams = topView.layoutParams
//                layoutParams.height = 0
//                topView.layoutParams = layoutParams
//            }
            return
        }
        activity.immersionBar {
            if (statusBar == null) {
                statusBarView(topView)
                autoStatusBarDarkModeEnable(true, 0.2f)
                //设置状态栏颜色
                if (title == null || title.backgroundColor == Config.NO_ASSIGNMENT) {
                    statusBarColor(globalConfig.getTitleBackground())
                } else {
                    statusBarColor(title.backgroundColor)
                }
            } else {
                checkStatusBarHide(this)
            }
        }
    }

    /**
     * 检查状态栏是否隐藏
     */
    private fun checkStatusBarHide(immersionBar: ImmersionBar) {
        if (statusBar!!.hide) {
            //状态栏隐藏
            immersionBar.hideBar(BarHide.FLAG_HIDE_STATUS_BAR)
        } else {
            checkStatusBarTransparent(immersionBar)
        }
    }

    /**
     * 检查状态栏是否透明
     */
    private fun checkStatusBarTransparent(immersionBar: ImmersionBar) {
        if (statusBar!!.transparent) {
            immersionBar.transparentStatusBar()
        } else {
            immersionBar.statusBarView(R.id.top_view)
            immersionBar.autoStatusBarDarkModeEnable(true, 0.2f)
            immersionBar.statusBarColor(globalConfig.getTitleBackground())
        }
    }
}