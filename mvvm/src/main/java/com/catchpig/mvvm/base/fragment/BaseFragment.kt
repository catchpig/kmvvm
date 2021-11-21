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
import androidx.viewbinding.ViewBinding
import com.catchpig.mvvm.apt.KotlinMvvmCompiler
import com.catchpig.mvvm.base.activity.BaseActivity
import com.catchpig.mvvm.controller.LoadingViewController
import com.catchpig.mvvm.databinding.ViewRootBinding
import com.catchpig.mvvm.manager.SnackbarManager
import java.lang.reflect.ParameterizedType

/**
 * Fragment封装基类
 * @author catchpig
 * @date 2019/4/4 23:14
 */
open class BaseFragment<VB : ViewBinding> : Fragment() {
    protected val bodyBinding: VB by lazy {
        var type = javaClass.genericSuperclass
        var vbClass: Class<VB> = (type as ParameterizedType).actualTypeArguments[0] as Class<VB>
        val method = vbClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
        method.invoke(this, layoutInflater) as VB
    }

    private val loadingViewController: LoadingViewController by lazy {
        LoadingViewController(requireActivity(), KotlinMvvmCompiler.globalConfig(), rootBinding)
    }

    private val rootBinding: ViewRootBinding by lazy {
        ViewRootBinding.inflate(layoutInflater)
    }

    fun getRootBanding(): ViewRootBinding {
        return rootBinding
    }

    fun baseActivity(): BaseActivity<*>? {
        if (activity is BaseActivity<*>) {
            return activity as BaseActivity<*>
        }
        return null
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
        SnackbarManager.show(bodyBinding.root, text, gravity)
    }

    fun snackBar(@StringRes textRes: Int) {
        SnackbarManager.show(bodyBinding.root, textRes)
    }

    fun loadingView() {
        loadingViewController.loadingView()
    }

    fun loadingDialog() {
        loadingViewController.loadingDialog()
    }

    fun hideLoading() {
        loadingViewController.hideLoading()
    }
}
