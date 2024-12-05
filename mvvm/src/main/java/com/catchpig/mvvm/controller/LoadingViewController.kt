package com.catchpig.mvvm.controller

import android.app.Activity
import android.app.Dialog
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.catchpig.loading.view.LoadingView
import com.catchpig.mvvm.R
import com.catchpig.mvvm.databinding.ViewRootBinding
import com.catchpig.mvvm.interfaces.IGlobalConfig
import com.catchpig.mvvm.ksp.KotlinMvvmCompiler

/**
 *
 * @author TLi2
 **/
class LoadingViewController(
    private val activity: Activity,
    private val rootBinding: ViewRootBinding
) {
    private val globalConfig: IGlobalConfig = KotlinMvvmCompiler.globalConfig()
    private var dialog: Dialog? = null
    private var isLoadingInflate = false
    private lateinit var loadingFrame: FrameLayout
    private lateinit var loadingView: LoadingView
    fun loadingView() {
        rootBinding.layoutBody.run {
            if (isLoadingInflate) {
                loadingFrame.visibility = View.VISIBLE
                loadingFrame.setBackgroundResource(globalConfig.getLoadingBackground())
                loadingView.setLoadColor(globalConfig.getLoadingColor())
            } else {
                //setOnInflateListener监听器一定要在inflate()之前,不然会报空指针
                rootBinding.loadingViewStub.setOnInflateListener { _, view ->
                    isLoadingInflate = true
                    loadingFrame = view.findViewById(R.id.loading_frame);
                    loadingFrame.visibility = View.VISIBLE
                    loadingFrame.setBackgroundResource(globalConfig.getLoadingBackground())
                    loadingView = view.findViewById(R.id.loading_view)
                    loadingView.setLoadColor(globalConfig.getLoadingColor())
                }
                rootBinding.loadingViewStub.inflate()
            }
        }
    }

    fun loadingDialog() {
        dialog?.let {
            if (it.isShowing) {
                return
            }
        }
        dialog = Dialog(activity, R.style.loading_dialog_theme)
        dialog?.run {
            setCancelable(false)
            setContentView(R.layout.layout_loading)
            val loadingFrame = findViewById<FrameLayout>(R.id.loading_frame)
            val loadingView = findViewById<LoadingView>(R.id.loading_view)
            loadingFrame.visibility = View.VISIBLE
            loadingView.setLoadColor(globalConfig.getLoadingColor())
            setupSystemBar(activity, this)
            show()
            window?.run {
                val layoutParams = attributes
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
                layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
                decorView.setPadding(0, 0, 0, 0)
                attributes = layoutParams
            }
        }
    }

    private fun setupSystemBar(activity: Activity, dialog: Dialog) {
        dialog.window?.run {
            val windowInsetsControllerCompat =
                WindowInsetsControllerCompat(this, decorView)
            val windowInsetsCompat =
                WindowInsetsCompat.toWindowInsetsCompat(activity.window.decorView.rootWindowInsets)
            if (windowInsetsCompat.isVisible(WindowInsetsCompat.Type.statusBars())) {
                windowInsetsControllerCompat.show(WindowInsetsCompat.Type.statusBars())
            } else {
                windowInsetsControllerCompat.hide(WindowInsetsCompat.Type.statusBars())
            }
            if (windowInsetsCompat.isVisible(WindowInsetsCompat.Type.navigationBars())) {
                windowInsetsControllerCompat.show(WindowInsetsCompat.Type.navigationBars())
            } else {
                windowInsetsControllerCompat.hide(WindowInsetsCompat.Type.navigationBars())
            }
        }

    }

    fun hideLoading() {
        dialog?.let {
            it.dismiss()
            dialog = null
        }
        if (isLoadingInflate) {
            loadingFrame.visibility = View.GONE
        }
    }
}