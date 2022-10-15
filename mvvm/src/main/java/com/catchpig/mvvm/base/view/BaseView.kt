package com.catchpig.mvvm.base.view

import com.catchpig.mvvm.widget.refresh.RefreshRecyclerView
import kotlinx.coroutines.flow.Flow

internal interface BaseView {

    fun initParam()

    fun initView()

    fun initFlow()

    fun <T> lifecycleFlowRefresh(flow: Flow<MutableList<T>>, refresh: RefreshRecyclerView)

    fun <T> lifecycleFlow(
        flow: Flow<T>,
        errorCallback: ((t: Throwable) -> Unit)? = null,
        callback: T.() -> Unit
    )

    @Deprecated("")
    fun <T> lifecycleFlowLoadingView(
        flow: Flow<T>,
        errorCallback: ((t: Throwable) -> Unit)? = null,
        callback: T.() -> Unit
    )

    @Deprecated("")
    fun <T> lifecycleFlowLoadingDialog(
        flow: Flow<T>,
        errorCallback: ((t: Throwable) -> Unit)? = null,
        callback: T.() -> Unit
    )
}