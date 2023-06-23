package com.catchpig.mvvm.base.dialog

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.catchpig.mvvm.base.viewmodel.BaseViewModel
import java.lang.reflect.ParameterizedType

abstract class BaseVMDialogFragment<VB : ViewBinding, VM : BaseViewModel> :
    BaseDialogFragment<VB>() {
    protected val viewModel: VM by lazy {
        var type = javaClass.genericSuperclass
        var modelClass: Class<VM> = (type as ParameterizedType).actualTypeArguments[1] as Class<VM>
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[modelClass]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initParam()
        lifecycle.addObserver(viewModel)
        initView()
        initFlow()
    }

    abstract fun initParam()

    abstract fun initView()

    abstract fun initFlow()
}