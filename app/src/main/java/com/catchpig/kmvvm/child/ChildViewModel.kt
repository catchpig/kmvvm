package com.catchpig.kmvvm.child

import com.catchpig.mvvm.base.viewmodel.BaseViewModel
import com.catchpig.mvvm.ext.lifecycleLoadingDialog
import com.catchpig.mvvm.ext.lifecycleLoadingView
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class ChildViewModel : BaseViewModel() {

    fun loadingView(): Flow<String> {
        return flowOf("loadingView").map {
            delay(2000)
            it
        }
    }

    fun loadingViewViewModel() {
        loadingView().lifecycleLoadingView(this) {

        }
    }

    fun loadingDialog(): Flow<String> {
        return flowOf("loadingDialog").map {
            delay(2000)
            it
        }
    }

    fun loadingDialogViewModel() {
        loadingDialog().lifecycleLoadingDialog(this) {}
    }

    private var index = 0
    fun loadingViewError(): Flow<String> {
        index++
        return flowOf("loadingViewError").map {
            delay(2000)
            if (index > 1) {
                it
            } else {
                throw NullPointerException()
            }
        }
    }

    fun loadingViewErrorViewModel() {
        loadingViewError().lifecycleLoadingView(this, showFailedView = true) {

        }
    }
}