package com.catchpig.mvvm.base.activity

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.catchpig.mvvm.base.viewmodel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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
    }

    protected abstract fun initParam()
    protected abstract fun initView()
    protected abstract fun initObserver()

    fun <T> launcherLoadingView(flow: Flow<T>, callback: T.() -> Unit) {
        lifecycleScope.launch(Dispatchers.Main) {
            flow.flowOn(Dispatchers.IO).onStart {
                loadingView()
            }.onCompletion {
                hideLoadingView()
            }.catch { t: Throwable ->
                t.message?.let {
                    snackbar(it)
                }
            }.collect {
                callback(it)
            }
        }
    }

    fun <T> launcherLoadingDialog(flow: Flow<T>, callback: T.() -> Unit) {
        lifecycleScope.launch(Dispatchers.Main) {
            flow.flowOn(Dispatchers.IO).onStart {
                loadingDialog()
            }.onCompletion {
                hideLoadingView()
            }.catch { t: Throwable ->
                t.message?.let {
                    snackbar(it)
                }
            }.collect {
                callback(it)
            }
        }
    }
}
