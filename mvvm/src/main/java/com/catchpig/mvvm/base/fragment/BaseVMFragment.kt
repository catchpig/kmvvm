package com.catchpig.mvvm.base.fragment

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.catchpig.mvvm.apt.KotlinMvvmCompiler
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
 * @date 2019/4/6 11:25
 */
abstract class BaseVMFragment<VB : ViewBinding, VM : BaseViewModel> : BaseFragment<VB>(), BaseView {
    companion object {
        private const val TAG = "BaseVMFragment"
    }

    private val fullTag by lazy { "${javaClass.simpleName}_$TAG" }

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
        initFlow()
    }

    @Deprecated(
        "当前方法已废弃,请使用FlowExt.lifecycleRefresh()",
        replaceWith = ReplaceWith(
            expression = "flow.lifecycleRefresh(this,refresh)}"
        )
    )
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

    @Deprecated(
        "当前方法已废弃,请使用FlowExt.lifecycle()",
        replaceWith = ReplaceWith(
            expression = "flow.lifecycle(this,{\n\n}){\n\n}"
        )
    )
    override fun <T> lifecycleFlow(
        flow: Flow<T>,
        errorCallback: ((t: Throwable) -> Unit)?,
        callback: T.() -> Unit
    ) {
        lifecycleScope.launch(Dispatchers.Main)
        {
            flow.flowOn(Dispatchers.IO).catch { t: Throwable ->
                KotlinMvvmCompiler.onError(this@BaseVMFragment, t)
            }.onCompletion {
                "lifecycleFlow:onCompletion".logd(fullTag)
            }.catch { t ->
                errorCallback?.let {
                    errorCallback(t)
                }
            }.collect {
                callback(it)
            }
        }
    }

    @Deprecated(
        "当前方法已废弃,请使用FlowExt.lifecycleLoadingView()",
        replaceWith = ReplaceWith(
            expression = "flow.lifecycleLoadingView(this,{\n\n}){\n\n}"
        )
    )
    override fun <T> lifecycleFlowLoadingView(
        flow: Flow<T>,
        errorCallback: ((t: Throwable) -> Unit)?,
        callback: T.() -> Unit
    ) {
        lifecycleScope.launch(Dispatchers.Main) {
            flow.flowOn(Dispatchers.IO).onStart {
                loadingView()
            }.onCompletion {
                "lifecycleFlowLoadingView:onCompletion".logd(fullTag)
                hideLoading()
            }.catch { t ->
                KotlinMvvmCompiler.onError(this@BaseVMFragment, t)
                errorCallback?.let {
                    errorCallback(t)
                }
            }.collect {
                callback(it)
            }
        }

    }

    @Deprecated(
        "当前方法已废弃,请使用FlowExt.lifecycleLoadingDialog()",
        replaceWith = ReplaceWith(
            expression = "flow.lifecycleLoadingDialog(this,{\n\n}){\n\n}"
        )
    )
    override fun <T> lifecycleFlowLoadingDialog(
        flow: Flow<T>,
        errorCallback: ((t: Throwable) -> Unit)?,
        callback: T.() -> Unit
    ) {
        lifecycleScope.launch(Dispatchers.Main) {
            flow.flowOn(Dispatchers.IO).onStart {
                loadingDialog()
            }.onCompletion {
                "lifecycleFlowLoadingDialog:onCompletion".logd(this@BaseVMFragment.javaClass.simpleName)
                hideLoading()
            }.catch { t ->
                KotlinMvvmCompiler.onError(this@BaseVMFragment, t)
                errorCallback?.let {
                    errorCallback(t)
                }
            }.collect {
                callback(it)
            }
        }
    }
}
