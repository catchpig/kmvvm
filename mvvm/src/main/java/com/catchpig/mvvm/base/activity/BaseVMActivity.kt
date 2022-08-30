package com.catchpig.mvvm.base.activity

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.catchpig.mvvm.apt.KotlinMvvmCompiler
import com.catchpig.mvvm.base.fragment.BaseVMFragment
import com.catchpig.mvvm.base.view.BaseView
import com.catchpig.mvvm.base.viewmodel.BaseViewModel
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
abstract class BaseVMActivity<VB : ViewBinding, VM : BaseViewModel> : BaseActivity<VB>(), BaseView {
    companion object {
        private const val TAG = "BaseVMActivity"
    }
    private val fullTag by lazy {
        "${javaClass.simpleName}_${TAG}"
    }

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
        initFlow()
    }

    override fun <T> lifecycleFlowRefresh(
        flow: Flow<MutableList<T>>,
        refresh: RefreshRecyclerView
    ) {
        lifecycleScope.launch(Dispatchers.Main) {
            flow.flowOn(Dispatchers.IO).catch {
                refresh.updateError()
            }.onCompletion {
                "lifecycleFlowRefresh:onCompletion".logd(fullTag)
            }.collect {
                refresh.updateData(it)
            }
        }
    }

    override fun <T> lifecycleFlow(flow: Flow<T>, callback: T.() -> Unit) {
        lifecycleScope.launch(Dispatchers.Main) {
            flow.flowOn(Dispatchers.IO).catch { t: Throwable ->
                KotlinMvvmCompiler.onError(this@BaseVMActivity, t)
            }.onCompletion {
                "lifecycleFlow:onCompletion".logd(fullTag)
            }.collect {
                callback(it)
            }
        }
    }

    override fun <T> lifecycleFlowLoadingView(flow: Flow<T>, callback: T.() -> Unit) {
        lifecycleScope.launch(Dispatchers.Main) {
            flow.flowOn(Dispatchers.IO).onStart {
                loadingView()
            }.onCompletion {
                "lifecycleFlowLoadingView:onCompletion".logd(fullTag)
                hideLoadingView()
            }.catch { t: Throwable ->
                KotlinMvvmCompiler.onError(this@BaseVMActivity, t)
            }.collect {
                callback(it)
            }
        }
    }

    override fun <T> lifecycleFlowLoadingDialog(flow: Flow<T>, callback: T.() -> Unit) {
        lifecycleScope.launch(Dispatchers.Main) {
            flow.flowOn(Dispatchers.IO).onStart {
                loadingDialog()
            }.onCompletion {
                "lifecycleFlowLoadingDialog:onCompletion".logd(fullTag)
                hideLoadingView()
            }.catch { t: Throwable ->
                KotlinMvvmCompiler.onError(this@BaseVMActivity, t)
            }.collect {
                callback(it)
            }
        }
    }
}
