package com.catchpig.mvvm.base.view

import androidx.lifecycle.LifecycleCoroutineScope
import com.catchpig.mvvm.widget.refresh.RefreshRecyclerView
import kotlinx.coroutines.flow.Flow

interface BaseView {

    fun scope(): LifecycleCoroutineScope

    fun showFailedView()

    fun loadingDialog()

    fun loadingView()

    fun hideLoading()


}