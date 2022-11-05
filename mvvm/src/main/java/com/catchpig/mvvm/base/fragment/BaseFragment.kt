package com.catchpig.mvvm.base.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.catchpig.mvvm.R
import com.catchpig.mvvm.ksp.KotlinMvvmCompiler
import com.catchpig.mvvm.base.activity.BaseActivity
import com.catchpig.mvvm.base.view.BaseView
import com.catchpig.mvvm.controller.LoadingViewController
import com.catchpig.mvvm.databinding.ViewRootBinding
import com.catchpig.utils.ext.showSnackBar
import java.lang.reflect.ParameterizedType

/**
 * Fragment封装基类
 * @author catchpig
 * @date 2019/4/4 23:14
 */
open class BaseFragment<VB : ViewBinding> : Fragment(), BaseView {
    protected val bodyBinding: VB by lazy {
        var type = javaClass.genericSuperclass
        var vbClass: Class<VB> = (type as ParameterizedType).actualTypeArguments[0] as Class<VB>
        val method = vbClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
        method.invoke(this, layoutInflater) as VB
    }

    private val loadingViewController: LoadingViewController by lazy {
        LoadingViewController(requireActivity(), rootBinding)
    }

    private val rootBinding: ViewRootBinding by lazy {
        ViewRootBinding.inflate(layoutInflater)
    }

    private var failedBinding: ViewBinding? = null

    fun getRootBanding(): ViewRootBinding {
        return rootBinding
    }

    open fun rootBinding(block: ViewRootBinding.() -> Unit) {
        rootBinding.run(block)
    }

    open fun bodyBinding(block: VB.() -> Unit) {
        bodyBinding.run(block)
    }

    fun baseActivity(): BaseActivity<*>? {
        if (activity is BaseActivity<*>) {
            return activity as BaseActivity<*>
        }
        return null
    }

    inline fun <reified FVB : ViewBinding> failedBinding(block: FVB.() -> Unit) {
        getFailedBinding()?.let {
            (it as FVB).run(block)
        }
    }

    override fun scope(): LifecycleCoroutineScope {
        return lifecycleScope
    }

    override fun showFailedView() {
        getFailedBinding()?.let {
            rootBinding {
                layoutBody.addView(it.root)
            }
        }
    }

    override fun removeFailedView() {
        failedBinding?.let {
            rootBinding.layoutBody.removeView(it.root)
        }
    }

    override fun getFailedBinding(): ViewBinding? {
        if (failedBinding == null) {
            failedBinding = KotlinMvvmCompiler.globalConfig().getFailedBinding(layoutInflater, this)
        }
        return failedBinding
    }

    @CallSuper
    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootBinding.layoutBody.addView(
            bodyBinding.root,
            0,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        return rootBinding.root
    }

    fun snackBar(text: CharSequence, gravity: Int = Gravity.BOTTOM) {
        bodyBinding.root.showSnackBar(text, R.drawable.snackbar_bg, gravity)
    }

    fun snackBar(@StringRes textRes: Int, gravity: Int = Gravity.BOTTOM) {
        bodyBinding.root.showSnackBar(textRes, R.drawable.snackbar_bg, gravity)
    }

    override fun loadingView() {
        loadingViewController.loadingView()
    }

    override fun loadingDialog() {
        loadingViewController.loadingDialog()
    }

    override fun hideLoading() {
        loadingViewController.hideLoading()
    }
}
