package com.catchpig.mvvm.base.fragment

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.catchpig.mvvm.ksp.KotlinMvvmCompiler
import com.catchpig.mvvm.base.view.BaseVMView
import com.catchpig.mvvm.base.viewmodel.BaseViewModel
import com.catchpig.mvvm.widget.refresh.RefreshRecyclerView
import com.catchpig.utils.ext.logd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.reflect.ParameterizedType

/**
 * @author catchpig
 * @date 2019/4/6 11:25
 */
abstract class BaseVMFragment<VB : ViewBinding, VM : BaseViewModel> : BaseFragment<VB>(), BaseVMView {
    companion object {
        private const val TAG = "BaseVMFragment"
    }

    private val fullTag by lazy { "${javaClass.simpleName}_$TAG" }

    protected val viewModel: VM by lazy {
        var type = javaClass.genericSuperclass
        var modelClass: Class<VM> = (type as ParameterizedType).actualTypeArguments[1] as Class<VM>
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[modelClass]
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initParam()
        lifecycle.addObserver(viewModel)
        initView()
        initFlow()
    }
}
