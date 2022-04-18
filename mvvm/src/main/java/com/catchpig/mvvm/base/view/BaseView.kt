package com.catchpig.mvvm.base.view

import com.catchpig.mvvm.widget.refresh.RefreshRecyclerView
import kotlinx.coroutines.flow.Flow

interface BaseView {

    fun initParam()

    fun initView()

    fun initFlow()

    fun <T> lifecycleFlowRefresh(flow: Flow<MutableList<T>>, refresh: RefreshRecyclerView)

    fun <T> lifecycleFlow(flow: Flow<T>, callback: T.() -> Unit)

    fun <T> lifecycleFlowLoadingView(flow: Flow<T>, callback: T.() -> Unit)

    fun <T> lifecycleFlowLoadingDialog(flow: Flow<T>, callback: T.() -> Unit)
}