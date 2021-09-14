package com.catchpig.mvvm.controller

import android.app.Dialog
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import com.catchpig.loading.view.LoadingView
import com.catchpig.mvvm.R
import com.catchpig.mvvm.base.activity.BaseActivity
import com.catchpig.mvvm.databinding.ViewRootBinding
import com.catchpig.mvvm.ext.getLoadingColor
import com.catchpig.mvvm.ext.getLoadingViewBackground
import com.gyf.immersionbar.ImmersionBar

/**
 *
 * @author TLi2
 **/
class LoadingViewController(
    private val baseActivity: BaseActivity<*>,
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
                loadingFrame.setBackgroundResource(baseActivity.getLoadingViewBackground())
                loadingView.setLoadColor(baseActivity.getLoadingColor())
            } else {
                //setOnInflateListener监听器一定要在inflate()之前,不然会报空指针
                rootBinding.loadingViewStub.setOnInflateListener { _, view ->
                    isLoadingInflate = true
                    loadingFrame = view.findViewById(R.id.loading_frame);
                    loadingFrame.visibility = View.VISIBLE
                    loadingFrame.setBackgroundResource(baseActivity.getLoadingViewBackground())
                    loadingView = view.findViewById(R.id.loading_view)
                    loadingView.setLoadColor(baseActivity.getLoadingColor())
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
        dialog = Dialog(baseActivity, R.style.loading_dialog_theme)
        dialog?.run {
            setCancelable(false)
            setContentView(R.layout.layout_loading)
            val loadingFrame = findViewById<FrameLayout>(R.id.loading_frame);
            val loadingView = findViewById<LoadingView>(R.id.loading_view)
            loadingFrame.visibility = View.VISIBLE
            loadingView.setLoadColor(baseActivity.getLoadingColor())
            ImmersionBar.with(baseActivity, this).transparentBar().init()
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