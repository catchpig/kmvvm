package com.catchpig.kmvvm.mvvm.child

import com.catchpig.mvvm.base.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class ChildViewModel : BaseViewModel() {

    fun loadingView(): Flow<String> {
        return flowOf("loadingView")
    }

    fun loadingDialog(): Flow<String> {
        return flowOf("loadingDialog")
    }
}