package com.catchpig.mvvm.base.fragment

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.catchpig.mvvm.R
import com.catchpig.mvvm.base.viewmodel.BaseViewModel
import com.google.android.material.snackbar.Snackbar
import java.lang.reflect.ParameterizedType

/**
 * @author catchpig
 * @date 2019/4/6 11:25
 */
abstract class BaseVMFragment<VB : ViewBinding, VM : BaseViewModel> : BaseFragment<VB>() {
    protected val viewModel: VM by lazy {
        var type = javaClass.genericSuperclass
        var modelClass: Class<VM> = (type as ParameterizedType).actualTypeArguments[1] as Class<VM>
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(modelClass)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initParam()
        lifecycle.addObserver(viewModel)
        initView()
        initObserver()
        observerLoading()
        observerToast()
    }

    protected abstract fun initParam()

    protected abstract fun initView()

    protected abstract fun initObserver()

    private fun observerToast() {
        viewModel.toastLiveData.observe(this, {
            val snackbar = Snackbar.make(bodyBinding.root, it, Snackbar.LENGTH_LONG)
            snackbar.view.setBackgroundResource(R.color.color_toast_bg)
            snackbar.show()
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
