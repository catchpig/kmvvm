package com.catchpig.mvvm.interfaces

import com.catchpig.mvvm.base.activity.BaseActivity
import com.catchpig.mvvm.base.fragment.BaseFragment

interface IFlowError {
    /**
     * BaseFragment的会回调这个方法
     * @param baseFragment BaseFragment<*>
     * @param t Throwable
     */
    fun onBaseFragmentError(baseFragment: BaseFragment<*>, t: Throwable)

    /**
     * BaseActivity的会回调这个方法
     * @param baseActivity BaseActivity<*>
     * @param t Throwable
     */
    fun onBaseActivityError(baseActivity: BaseActivity<*>, t: Throwable)

    /**
     * BaseFragment和BaseActivity都会回调这个方法
     * @param any Any
     * @param t Throwable
     */
    fun onError(any: Any, t: Throwable)
}