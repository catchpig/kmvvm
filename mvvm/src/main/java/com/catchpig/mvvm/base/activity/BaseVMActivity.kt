package com.catchpig.mvvm.base.activity

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.catchpig.mvvm.base.viewmodel.BaseViewModel
import java.lang.reflect.ParameterizedType

/**
 * @author catchpig
 * @date 2019/4/6 11:07
 */
abstract class BaseVMActivity<VB : ViewBinding, VM : BaseViewModel> : BaseActivity<VB>() {

    val viewModel: VM by lazy {
        var type = javaClass.genericSuperclass
        var modelClass: Class<VM> = (type as ParameterizedType).actualTypeArguments[1] as Class<VM>
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(modelClass)
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initParam()
        lifecycle.addObserver(viewModel)
        initView()
        initObserver()
        observerLoading()
        observerMessage()
    }

    protected abstract fun initParam()
    protected abstract fun initView()
    protected abstract fun initObserver()

    private fun observerMessage() {
        viewModel.messageLiveData.observe(this, {
            snackbar(it)
        })
    }

    private fun observerLoading() {
        viewModel.showLoadingLiveData.observe(this, {
            loadingView(it)
        })
        viewModel.hideLoadingLiveData.observe(this, {
            hideLoadingView()
        })
    }
}
