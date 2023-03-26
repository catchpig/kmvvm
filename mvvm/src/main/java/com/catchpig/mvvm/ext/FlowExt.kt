package com.catchpig.mvvm.ext

import androidx.lifecycle.viewModelScope
import com.catchpig.mvvm.base.view.BaseView
import com.catchpig.mvvm.base.viewmodel.BaseViewModel
import com.catchpig.mvvm.ksp.KotlinMvvmCompiler
import com.catchpig.mvvm.widget.refresh.RefreshRecyclerView
import com.catchpig.utils.ext.logd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private const val TAG = "FlowExt"

fun <T> Flow<MutableList<T>>.lifecycleRefresh(
    base: BaseView,
    refreshLayoutWrapper: RefreshRecyclerView
) {
    base.scope().launch(Dispatchers.Main) {
        this@lifecycleRefresh.flowOn(Dispatchers.IO).catch {
            refreshLayoutWrapper.updateError()
        }.onCompletion {
            "$TAG:lifecycleRefresh:onCompletion".logd(base::class.simpleName!!)
        }.collect {
            refreshLayoutWrapper.updateData(it)
        }
    }
}

fun <T> Flow<T>.lifecycle(
    base: BaseView,
    showFailedView: Boolean = false,
    errorCallback: ((t: Throwable) -> Unit)? = null,
    callback: T.() -> Unit
) {
    base.scope().launch(Dispatchers.Main) {
        this@lifecycle.flowOn(Dispatchers.IO).onCompletion {
            "$TAG:lifecycle:onCompletion".logd(base::class.simpleName!!)
        }.catch { t ->
            if (showFailedView) {
                base.showFailedView()
            }
            KotlinMvvmCompiler.onError(base, t)
            errorCallback?.let {
                errorCallback(t)
            }
        }.collect {
            callback(it)
        }
    }
}

fun <T> Flow<T>.lifecycleLoadingDialog(
    base: BaseView,
    showFailedView: Boolean = false,
    errorCallback: ((t: Throwable) -> Unit)? = null,
    callback: T.() -> Unit
) {
    base.scope().launch(Dispatchers.Main) {
        this@lifecycleLoadingDialog.flowOn(Dispatchers.IO).onStart {
            base.loadingDialog()
            if (showFailedView) {
                base.removeFailedView()
            }
        }.onCompletion {
            "$TAG:lifecycleLoadingDialog:onCompletion".logd(base::class.simpleName!!)
            base.hideLoading()
        }.catch { t ->
            if (showFailedView) {
                base.showFailedView()
            }
            KotlinMvvmCompiler.onError(base, t)
            errorCallback?.let {
                errorCallback(t)
            }
        }.collect {
            callback(it)
        }
    }
}

fun <T> Flow<T>.lifecycleLoadingView(
    base: BaseView,
    showFailedView: Boolean = false,
    errorCallback: ((t: Throwable) -> Unit)? = null,
    callback: T.() -> Unit
) {
    base.scope().launch(Dispatchers.Main) {
        this@lifecycleLoadingView.flowOn(Dispatchers.IO).onStart {
            base.loadingView()
            if (showFailedView) {
                base.removeFailedView()
            }
        }.onCompletion {
            "$TAG:lifecycleLoadingView:onCompletion".logd(base::class.simpleName!!)
            base.hideLoading()
        }.catch { t ->
            if (showFailedView) {
                base.showFailedView()
            }
            KotlinMvvmCompiler.onError(base, t)
            errorCallback?.let {
                errorCallback(t)
            }
        }.collect {
            callback(it)
        }
    }
}

fun <T> Flow<T>.lifecycle(
    baseViewModel: BaseViewModel,
    showFailedView: Boolean = false,
    errorCallback: ((t: Throwable) -> Unit)? = null,
    callback: T.() -> Unit
) {
    baseViewModel.viewModelScope.launch(Dispatchers.Main) {
        this@lifecycle.flowOn(Dispatchers.IO).onCompletion {
            "$TAG:lifecycle:onCompletion".logd(baseViewModel::class.simpleName!!)
        }.catch { t ->
            baseViewModel.errorLiveData.value = t
            if (showFailedView) {
                baseViewModel.showFailedViewLiveData.value = true
            }
            errorCallback?.let {
                errorCallback(t)
            }
        }.collect {
            callback(it)
        }
    }
}

fun <T> Flow<T>.lifecycleLoadingView(
    baseViewModel: BaseViewModel,
    showFailedView: Boolean = false,
    errorCallback: ((t: Throwable) -> Unit)? = null,
    callback: T.() -> Unit
) {
    baseViewModel.viewModelScope.launch(Dispatchers.Main) {
        this@lifecycleLoadingView.flowOn(Dispatchers.IO).onStart {
            baseViewModel.loadingViewLiveData.value = true
            if (showFailedView) {
                baseViewModel.showFailedViewLiveData.value = false
            }
        }.onCompletion {
            "$TAG:lifecycleLoadingView:onCompletion".logd(baseViewModel::class.simpleName!!)
            baseViewModel.loadingViewLiveData.value = false
            baseViewModel.hideLoadingLiveData.value = true
        }.catch { t ->
            if (showFailedView) {
                baseViewModel.showFailedViewLiveData.value = true
            }
            baseViewModel.hideLoadingLiveData.value = true
            errorCallback?.let {
                errorCallback(t)
            }
        }.collect {
            callback(it)
        }
    }
}


fun <T> Flow<T>.lifecycleLoadingDialog(
    baseViewModel: BaseViewModel,
    showFailedView: Boolean = false,
    errorCallback: ((t: Throwable) -> Unit)? = null,
    callback: T.() -> Unit
) {
    baseViewModel.viewModelScope.launch(Dispatchers.Main) {
        this@lifecycleLoadingDialog.flowOn(Dispatchers.IO).onStart {
            baseViewModel.loadingDialogLiveData.value = true
            if (showFailedView) {
                baseViewModel.showFailedViewLiveData.value = false
            }
        }.onCompletion {
            "$TAG:lifecycleLoadingDialog:onCompletion".logd(baseViewModel::class.simpleName!!)
            baseViewModel.loadingDialogLiveData.value = false
            baseViewModel.hideLoadingLiveData.value = true
        }.catch { t ->
            if (showFailedView) {
                baseViewModel.showFailedViewLiveData.value = true
            }
            baseViewModel.errorLiveData.value = t
            errorCallback?.let {
                errorCallback(t)
            }
        }.collect {
            callback(it)
        }
    }
}

