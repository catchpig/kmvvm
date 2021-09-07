package com.catchpig.mvvm.controller

import com.catchpig.mvvm.R
import com.catchpig.mvvm.base.activity.BaseActivity
import com.catchpig.mvvm.config.Config
import com.catchpig.mvvm.entity.StatusBarParam
import com.catchpig.mvvm.entity.TitleParam
import com.catchpig.mvvm.ext.getTitleBackground
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.ktx.immersionBar

/**
 * 状态栏处理器
 * @author catchpig
 * @date 2019/8/18 00:18
 */
class StatusBarController(private val baseActivity: BaseActivity, private val title: TitleParam?, private val statusBar: StatusBarParam?) {

    fun checkStatusBar() {
        //状态栏注解设置为不可用
        if (statusBar != null && statusBar.enabled) {
            return
        }
        baseActivity.immersionBar {
            if (statusBar == null) {
                statusBarView(R.id.top_view)
                autoStatusBarDarkModeEnable(true, 0.2f)
                //设置状态栏颜色
                if (title == null || title.backgroundColor == Config.NO_ASSIGNMENT) {
                    statusBarColor(baseActivity.getTitleBackground())
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
            immersionBar.statusBarColor(baseActivity.getTitleBackground())
        }
    }
}