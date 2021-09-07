package com.catchpig.mvvm.base.fragment

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModelProvider
import com.catchpig.mvvm.base.viewmodel.BaseViewModel
import java.lang.reflect.ParameterizedType

/**
 * @author catchpig
 * @date 2019/4/6 11:25
 */
abstract class BaseViewModelFragment<VM : BaseViewModel> : BaseFragment()  {
    val viewModel:VM by lazy {
        var type = javaClass.genericSuperclass
        var modelClass:Class<VM> = (type as ParameterizedType).actualTypeArguments[0] as Class<VM>
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(modelClass)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initParam()
        lifecycle.addObserver(viewModel)
        initView()
    }


    protected abstract fun initParam()


    protected abstract fun initView()
}
