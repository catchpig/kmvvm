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

/**
 * 列表刷新的lifecycle
 * @receiver Flow<MutableList<T>>
 * @param base BaseView
 * @param refreshLayoutWrapper RefreshRecyclerView
 */
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

/**
 * 不带loading的lifecycle
 * @receiver Flow<T>
 * @param base BaseView
 * @param showFailedView Boolean 是否显示请求失败的页面
 * @param errorCallback Function1<[@kotlin.ParameterName] Throwable, Unit>? 异常的回调
 * @param callback [@kotlin.ExtensionFunctionType] Function1<T, Unit> 正常数据的回调
 */
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

/**
 * 带loadingDialog的lifecycle
 * @receiver Flow<T>
 * @param base BaseView
 * @param showFailedView Boolean 是否显示请求失败的页面
 * @param errorCallback Function1<[@kotlin.ParameterName] Throwable, Unit>? 异常的回调
 * @param callback [@kotlin.ExtensionFunctionType] Function1<T, Unit> 正常数据的回调
 */
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

/**
 * 带loadingView的lifecycle
 * @receiver Flow<T>
 * @param base BaseView
 * @param showFailedView Boolean 是否显示请求失败的页面
 * @param errorCallback Function1<[@kotlin.ParameterName] Throwable, Unit>? 异常的回调
 * @param callback [@kotlin.ExtensionFunctionType] Function1<T, Unit> 正常数据的回调
 */
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

/**
 * 不带loading的lifecycle
 * @receiver Flow<T>
 * @param baseViewModel BaseViewModel
 * @param showFailedView Boolean 是否显示请求失败的页面
 * @param errorCallback Function1<[@kotlin.ParameterName] Throwable, Unit>? 异常的回调
 * @param callback [@kotlin.ExtensionFunctionType] Function1<T, Unit> 正常数据的回调
 */
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

/**
 * 带loadingDialog的lifecycle
 * @receiver Flow<T>
 * @param baseViewModel BaseViewModel
 * @param showFailedView Boolean 是否显示请求失败的页面
 * @param errorCallback Function1<[@kotlin.ParameterName] Throwable, Unit>? 异常的回调
 * @param callback [@kotlin.ExtensionFunctionType] Function1<T, Unit> 正常数据的回调
 */
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

/**
 * 带loadingView的lifecycle
 * @receiver Flow<T>
 * @param baseViewModel BaseViewModel
 * @param showFailedView Boolean 是否显示请求失败的页面
 * @param errorCallback Function1<[@kotlin.ParameterName] Throwable, Unit>? 异常的回调
 * @param callback [@kotlin.ExtensionFunctionType] Function1<T, Unit> 正常数据的回调
 */
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

