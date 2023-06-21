package com.catchpig.mvvm.base.activity

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.catchpig.mvvm.base.view.BaseVMView
import com.catchpig.mvvm.base.viewmodel.BaseViewModel
import com.catchpig.mvvm.ksp.KotlinMvvmCompiler
import com.catchpig.mvvm.widget.refresh.RefreshRecyclerView
import com.catchpig.utils.ext.logd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.reflect.ParameterizedType

/**
 * @author catchpig
 * @date 2019/4/6 11:07
 */
abstract class BaseVMActivity<VB : ViewBinding, VM : BaseViewModel> : BaseActivity<VB>(),
    BaseVMView {
    companion object {
        private const val TAG = "BaseVMActivity"
    }

    private val fullTag by lazy {
        "${javaClass.simpleName}_${TAG}"
    }

    val viewModel: VM by lazy {
        var type = javaClass.genericSuperclass
        var modelClass: Class<VM> = (type as ParameterizedType).actualTypeArguments[1] as Class<VM>
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
        viewModel.loadingDialogLiveData.observe(this) {
            if (it) {
                loadingDialog()
            }
        }
        viewModel.loadingViewLiveData.observe(this) {
            if (it) {
                loadingView()
            }
        }
        viewModel.hideLoadingLiveData.observe(this) {
            if (it) {
                hideLoading()
            }
        }
        viewModel.errorLiveData.observe(this) {
            if (it != null) {
                KotlinMvvmCompiler.onError(this, it)
            }
        }
        viewModel.showFailedViewLiveData.observe(this){
            if (it) {
                showFailedView()
            }else{
                removeFailedView()
            }
        }
    }
}
