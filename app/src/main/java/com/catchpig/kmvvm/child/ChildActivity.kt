package com.catchpig.kmvvm.child

import android.view.View
import com.catchpig.annotation.*
import com.catchpig.kmvvm.R
import com.catchpig.kmvvm.databinding.ActivityChildBinding
import com.catchpig.mvvm.base.activity.BaseVMActivity
import com.catchpig.mvvm.manager.SnackbarManager

@Title(R.string.child_title)
class ChildActivity : BaseVMActivity<ActivityChildBinding, ChildViewModel>() {
    @OnClickFirstDrawable(R.drawable.more)
    fun clickFirstDrawable(v: View) {
        SnackbarManager.show(bodyBinding.root, "第一个图标按钮点击生效")
        updateTitle("nihao")
    }

    @OnClickFirstText(R.string.more)
    fun clickFirstText() {
        SnackbarManager.show(bodyBinding.root, "第一个文字按钮点击生效")
        updateTitle("12354")
    }

    @OnClickSecondDrawable(R.drawable.more)
    fun clickSecondDrawable(v: View) {
        SnackbarManager.show(bodyBinding.root, "第一个图标按钮点击生效")
        updateTitle("nihao")
    }

    @OnClickSecondText(R.string.more)
    fun clickSecondText() {
        SnackbarManager.show(bodyBinding.root, "第一个文字按钮点击生效")
        updateTitle("12354")
    }

    override fun initParam() {

    }

    override fun initView() {

    }

    override fun initFlow() {

    }

    /**
     * dialog形式的loading
     */
    fun loadingDialog(v: View) {
        lifecycleFlowLoadingDialog(viewModel.loadingDialog()) {
            snackBar(this)
        }

    }

    /**
     * 标题栏以下的loading
     */
    fun loadingView(v: View) {
        lifecycleFlowLoadingView(viewModel.loadingView()) {
            snackBar(this)
        }
    }
}
