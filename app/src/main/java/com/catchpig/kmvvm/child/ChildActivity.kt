package com.catchpig.kmvvm.child

import android.view.View
import com.catchpig.annotation.*
import com.catchpig.kmvvm.R
import com.catchpig.kmvvm.databinding.ActivityChildBinding
import com.catchpig.mvvm.base.activity.BaseVMActivity
import com.catchpig.mvvm.ext.lifecycleLoadingDialog
import com.catchpig.mvvm.ext.lifecycleLoadingView

@Title(R.string.child_title)
class ChildActivity : BaseVMActivity<ActivityChildBinding, ChildViewModel>() {
    @OnClickFirstDrawable(R.drawable.more)
    fun clickFirstDrawable(v: View) {
        snackBar("第一个图标按钮点击生效")
        updateTitle("第一图标")
    }

    @OnClickFirstText(R.string.more)
    fun clickFirstText() {
        snackBar("第一个文字按钮点击生效")
        updateTitle("第一文字")
    }

    @OnClickSecondDrawable(R.drawable.more)
    fun clickSecondDrawable(v: View) {
        snackBar("第二个图标按钮点击生效")
        updateTitle("第二图标")
    }

    @OnClickSecondText(R.string.more)
    fun clickSecondText() {
        snackBar("第二个文字按钮点击生效")
        updateTitle("第二文字")
    }

    override fun initParam() {

    }

    override fun initView() {

    }

    override fun initFlow() {
        onFailedReload {
            loadingViewError(bodyBinding.root)
        }
    }

    /**
     * dialog形式的loading
     */
    fun loadingDialog(v: View) {
        viewModel.loadingDialog().lifecycleLoadingDialog(this) {
            snackBar(this)
        }
    }

    fun loadingViewError(v: View) {
        viewModel.loadingViewError().lifecycleLoadingView(this, showFailedView = true) {
            snackBar(this)
        }
    }

    /**
     * 标题栏以下的loading
     */
    fun loadingView(v: View) {
        viewModel.loadingView().lifecycleLoadingView(this) {
            snackBar(this)
        }
    }
}
