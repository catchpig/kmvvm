package com.catchpig.mvvm.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.catchpig.mvvm.base.activity.BaseActivity
import java.lang.reflect.ParameterizedType

/**
 * Fragment封装基类
 * @author catchpig
 * @date 2019/4/4 23:14
 */
open class BaseFragment<VB : ViewBinding> : Fragment() {
    val bodyBinding: VB by lazy {
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

    fun loadingView(isDialog: Boolean) {
        baseActivity()?.let {
            it.loadingView(isDialog)
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

    fun toast(text: String, isLong: Boolean) {
        baseActivity()?.let {
            it.toast(text, isLong)
        }
    }
}
