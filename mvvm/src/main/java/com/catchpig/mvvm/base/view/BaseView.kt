package com.catchpig.mvvm.base.view

import android.view.View
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.viewbinding.ViewBinding
import com.catchpig.mvvm.apt.KotlinMvvmCompiler

interface BaseView {

    fun scope(): LifecycleCoroutineScope

    fun showFailedView()

    fun loadingDialog()

    fun loadingView()

    fun hideLoading()

    fun getFailedBinding(): ViewBinding?

    fun removeFailedView()

    /**
     * 加载失败后展示失败页面,点击自定义失败页面的刷新按钮,重新请求数据
     * @param autoFirstLoad Boolean 第一次是否自动加载
     * @param block [@kotlin.ExtensionFunctionType] Function1<View, Unit>
     */
    fun onFailedReload(autoFirstLoad: Boolean = true, block: View.() -> Unit) {
        val failedBinding = getFailedBinding()
        failedBinding?.let { viewBinding ->
            val failedRootView = viewBinding.root
            val clickView = failedRootView.findViewById<View>(
                KotlinMvvmCompiler.globalConfig().onFailedReloadClickId()
            )
            if (autoFirstLoad) {
                clickView.run(block)
            }
            clickView.setOnClickListener {
                it.run(block)
            }
        }
    }
}