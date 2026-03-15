package com.catchpig.mvvm.base.activity

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.catchpig.mvvm.base.view.BaseVMView
import com.catchpig.mvvm.base.viewmodel.BaseViewModel
import com.catchpig.mvvm.ksp.KotlinMvvmCompiler
import java.lang.reflect.ParameterizedType

/**
 * @author catchpig
 * @date 2019/4/6 11:07
 */
abstract class BaseVMActivity<VB : ViewBinding, VM : BaseViewModel> : BaseActivity<VB>(),
    BaseVMView {

    val viewModel: VM by lazy {
        @Suppress("UNCHECKED_CAST")
        val modelClass = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<VM>
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[modelClass]
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initParam()
        lifecycle.addObserver(viewModel)
        initLoadingObserver()
        initView()
        initFlow()
    }

    private fun initLoadingObserver() {
        viewModel.loadingDialogLiveData.observe(this) { if (it) loadingDialog() }
        viewModel.loadingViewLiveData.observe(this) { if (it) loadingView() }
        viewModel.hideLoadingLiveData.observe(this) { if (it) hideLoading() }
        viewModel.errorLiveData.observe(this) { error ->
            error?.let { KotlinMvvmCompiler.onError(this, it) }
        }
        viewModel.showFailedViewLiveData.observe(this) { show ->
            if (show) showFailedView() else removeFailedView()
        }
    }
}
