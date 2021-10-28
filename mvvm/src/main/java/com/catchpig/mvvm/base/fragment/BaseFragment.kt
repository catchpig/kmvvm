package com.catchpig.mvvm.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.catchpig.mvvm.base.activity.BaseActivity
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
        return bodyBinding.root
    }

    protected fun snackbar(text: CharSequence) {
        SnackbarManager.show(bodyBinding.root, text)
    }

    protected fun snackbar(@StringRes textRes: Int) {
        SnackbarManager.show(bodyBinding.root, textRes)
    }

    fun loadingView() {
        baseActivity()?.let {
            it.loadingView()
        }
    }

    fun loadingDialog() {
        baseActivity()?.let {
            it.loadingDialog()
        }
    }

    fun hideLoadingView() {
        baseActivity()?.let {
            it.hideLoadingView()
        }
    }

    fun closeActivity() {
        activity?.let {
            it.finish()
        }
    }
}
