package com.catchpig.mvvm.controller

import android.app.Dialog
import android.view.*
import android.widget.FrameLayout
import com.catchpig.mvvm.R
import com.catchpig.mvvm.base.activity.BaseActivity
import com.catchpig.mvvm.ext.getLoadingViewBackground
import com.catchpig.mvvm.ext.getLoadingColor
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.layout_loading.*
import kotlinx.android.synthetic.main.layout_loading.view.*
import kotlinx.android.synthetic.main.view_root.view.*

/**
 *
 * @author TLi2
 **/
class LoadingViewController(private val baseActivity: BaseActivity<*>,private val layoutBody:FrameLayout) {
    private var dialog:Dialog? = null
    private var isLoadingInflate = false
    fun loadingView(){
        layoutBody.run {
            if (isLoadingInflate) {
                loading_frame.visibility = View.VISIBLE
                loading_frame.setBackgroundResource(baseActivity.getLoadingViewBackground())
                loading_view.setLoadColor(baseActivity.getLoadingColor())
            }else{
                //setOnInflateListener监听器一定要在inflate()之前,不然会报空指针
                loading_view_stub.setOnInflateListener { _, _ ->
                    isLoadingInflate = true
                    loading_frame.visibility = View.VISIBLE
                    loading_frame.setBackgroundResource(baseActivity.getLoadingViewBackground())
                    loading_view.setLoadColor(baseActivity.getLoadingColor())
                }
                loading_view_stub.inflate()
            }
        }
    }

    fun loadingDialog(){
        dialog = Dialog(baseActivity, R.style.loading_dialog_theme)
        dialog?.run {
            setCancelable(false)
            setContentView(R.layout.layout_loading)
            loading_frame.visibility = View.VISIBLE
            loading_view.setLoadColor(baseActivity.getLoadingColor())
            ImmersionBar.with(baseActivity,this).transparentBar().init()
            show()
            window?.run {
                var layoutParams = attributes
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
                layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
                decorView.setPadding(0,0,0,0)
                attributes = layoutParams
            }
        }
    }

    fun hideLoading(){
        dialog?.let {
            it.dismiss()
        }
        if (isLoadingInflate) {
            layoutBody.loading_frame.visibility = View.GONE
        }
    }
}