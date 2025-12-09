package com.catchpig.mvvm.base.dialog

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.catchpig.mvvm.base.view.BaseVMView
import com.catchpig.mvvm.base.viewmodel.BaseViewModel
import com.catchpig.mvvm.ksp.KotlinMvvmCompiler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.reflect.ParameterizedType
import kotlin.coroutines.CoroutineContext

abstract class BaseVMDialogFragment<VB : ViewBinding, VM : BaseViewModel> :
    BaseDialogFragment<VB>(), BaseVMView {
    protected val viewModel: VM by lazy {
        val type = javaClass.genericSuperclass
        val modelClass: Class<VM> = (type as ParameterizedType).actualTypeArguments[1] as Class<VM>
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[modelClass]
    }
    private var failedBinding: ViewBinding? = null

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initParam()
        lifecycle.addObserver(viewModel)
        initView()
        initFlow()
    }

    open fun bodyBinding(block: VB.() -> Unit) {
        bodyBinding.run(block)
    }

    override fun getFailedBinding(): ViewBinding? {
        if (failedBinding == null) {
            failedBinding = KotlinMvvmCompiler.globalConfig().getFailedBinding(layoutInflater, this)
        }
        return failedBinding
    }

    override fun launcherOnLifecycle(
        context: CoroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return lifecycleScope.launch(context) {
            block()
        }
    }

    override fun repeatLauncherOnLifecycle(
        context: CoroutineContext,
        state: Lifecycle.State,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return lifecycleScope.launch(context) {
            repeatOnLifecycle(state) {
                block()
            }
        }
    }

    override fun showFailedView() {

    }

    override fun removeFailedView() {

    }

    override fun snackBar(text: CharSequence, gravity: Int) {
    }

    override fun snackBar(@StringRes textRes: Int, gravity: Int) {
    }

    override fun loadingDialog() {

    }

    override fun loadingView() {

    }

    override fun hideLoading() {

    }
}