package com.catchpig.mvvm.controller

import android.app.Activity
import android.app.Dialog
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import com.catchpig.loading.view.LoadingView
import com.catchpig.mvvm.R
import com.catchpig.mvvm.databinding.ViewRootBinding
import com.catchpig.mvvm.interfaces.IGlobalConfig
import com.gyf.immersionbar.ImmersionBar

/**
 *
 * @author TLi2
 **/
class LoadingViewController(
    private val activity: Activity,
    private val iGlobalConfig: IGlobalConfig,
    private val rootBinding: ViewRootBinding
) {
    private var dialog: Dialog? = null
    private var isLoadingInflate = false
    private lateinit var loadingFrame: FrameLayout
    private lateinit var loadingView: LoadingView
    fun loadingView() {
        rootBinding.layoutBody.run {
            if (isLoadingInflate) {
                loadingFrame.visibility = View.VISIBLE
                loadingFrame.setBackgroundResource(iGlobalConfig.getLoadingBackground())
                loadingView.setLoadColor(iGlobalConfig.getLoadingColor())
            } else {
                //setOnInflateListener监听器一定要在inflate()之前,不然会报空指针
                rootBinding.loadingViewStub.setOnInflateListener { _, view ->
                    isLoadingInflate = true
                    loadingFrame = view.findViewById(R.id.loading_frame);
                    loadingFrame.visibility = View.VISIBLE
                    loadingFrame.setBackgroundResource(iGlobalConfig.getLoadingBackground())
                    loadingView = view.findViewById(R.id.loading_view)
                    loadingView.setLoadColor(iGlobalConfig.getLoadingColor())
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
            val loadingFrame = findViewById<FrameLayout>(R.id.loading_frame);
            val loadingView = findViewById<LoadingView>(R.id.loading_view)
            loadingFrame.visibility = View.VISIBLE
            loadingView.setLoadColor(iGlobalConfig.getLoadingColor())
            ImmersionBar.with(activity, this).transparentBar().init()
            show()
            window?.run {
                var layoutParams = attributes
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
                layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
                decorView.setPadding(0, 0, 0, 0)
                attributes = layoutParams
            }
        }
    }

    fun hideLoading() {
        dialog?.let {
            it.dismiss()
        }
        if (isLoadingInflate) {
            loadingFrame.visibility = View.GONE
        }
    }
}