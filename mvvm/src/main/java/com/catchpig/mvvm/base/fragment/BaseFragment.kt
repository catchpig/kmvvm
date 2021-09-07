package com.catchpig.mvvm.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.catchpig.mvvm.base.activity.BaseActivity

/**
 * Fragment封装基类
 * @author catchpig
 * @date 2019/4/4 23:14
 */
abstract class BaseFragment : Fragment() {
    fun baseActivity(): BaseActivity? {
        if (activity is BaseActivity) {
            return activity as BaseActivity
        }
        return null
    }

    @CallSuper
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    @CallSuper
    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutId(), container, false)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
    }

    @CallSuper
    override fun onStop() {
        super.onStop()
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
    }

    @LayoutRes
    protected abstract fun layoutId(): Int

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
