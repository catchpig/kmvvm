package com.catchpig.mvvm.ext

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewModelScope
import com.catchpig.mvvm.base.view.BaseView
import com.catchpig.mvvm.base.viewmodel.BaseViewModel
import com.catchpig.mvvm.ksp.KotlinMvvmCompiler
import com.catchpig.mvvm.widget.refresh.RefreshRecyclerView
import com.catchpig.utils.ext.logd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

private const val TAG = "FlowExt"

/**
 * 列表刷新的lifecycle
 * @receiver Flow<MutableList<T>>
 * @param base BaseView
 * @param refreshLayoutWrapper RefreshRecyclerView
 * @param callback [@kotlin.ExtensionFunctionType] Function1<T, Unit> 正常数据的回调
 */
fun <T> Flow<MutableList<T>>.lifecycleRefresh(
    base: BaseView,
    refreshLayoutWrapper: RefreshRecyclerView,
    callback: (MutableList<T>.() -> Unit)? = null
) {
    base.launcherOnLifecycle(Dispatchers.Main) {
        lifecycleRefresh(
            this@lifecycleRefresh,
            base::class.simpleName!!,
            refreshLayoutWrapper,
            callback
        )
    }
}

/**
 * 列表刷新的lifecycle
 * @receiver Flow<MutableList<T>>
 * @param base BaseView
 * @param refreshLayoutWrapper RefreshRecyclerView
 * @param state Lifecycle.State 指定在哪个生命周期才回调数据(默认在前台才回调数据)
 * @param callback [@kotlin.ExtensionFunctionType] Function1<T, Unit> 正常数据的回调
 */
fun <T> Flow<MutableList<T>>.repeatOnLifecycleRefresh(
    base: BaseView,
    refreshLayoutWrapper: RefreshRecyclerView,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    callback: (MutableList<T>.() -> Unit)? = null
) {
    base.repeatLauncherOnLifecycle(Dispatchers.Main, state) {
        lifecycleRefresh(
            this@repeatOnLifecycleRefresh,
            base::class.simpleName!!,
            refreshLayoutWrapper,
            callback
        )
    }
}

private suspend fun <T> lifecycleRefresh(
    flow: Flow<MutableList<T>>,
    className: String,
    refreshLayoutWrapper: RefreshRecyclerView,
    callback: (MutableList<T>.() -> Unit)? = null
) {
    flow.flowOn(Dispatchers.IO).catch {
        refreshLayoutWrapper.updateError()
    }.onCompletion {
        "$TAG:lifecycleRefresh:onCompletion".logd(className)
    }.collect { list ->
        refreshLayoutWrapper.updateData(list)
        callback?.let {
            it(list)
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
fun <T> Flow<T?>.lifecycleNull(
    base: BaseView,
    showFailedView: Boolean = false,
    errorCallback: ((t: Throwable) -> Unit)? = null,
    callback: T?.() -> Unit
) {
    base.launcherOnLifecycle(Dispatchers.Main) {
        lifecycleNull(this@lifecycleNull, base, showFailedView, errorCallback, callback)
    }
}

/**
 * 不带loading的lifecycle
 * @receiver Flow<T>
 * @param base BaseView
 * @param showFailedView Boolean 是否显示请求失败的页面
 * @param state Lifecycle.State 指定在哪个生命周期才回调数据(默认在前台才回调数据)
 * @param errorCallback Function1<[@kotlin.ParameterName] Throwable, Unit>? 异常的回调
 * @param callback [@kotlin.ExtensionFunctionType] Function1<T, Unit> 正常数据的回调
 */
fun <T> Flow<T?>.repeatOnLifecycleNull(
    base: BaseView,
    showFailedView: Boolean = false,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    errorCallback: ((t: Throwable) -> Unit)? = null,
    callback: T?.() -> Unit
) {
    base.repeatLauncherOnLifecycle(Dispatchers.Main, state) {
        lifecycleNull(this@repeatOnLifecycleNull, base, showFailedView, errorCallback, callback)
    }
}

private suspend fun <T> lifecycleNull(
    flow: Flow<T?>,
    base: BaseView,
    showFailedView: Boolean = false,
    errorCallback: ((t: Throwable) -> Unit)? = null,
    callback: T?.() -> Unit
) {
    flow.flowOn(Dispatchers.IO).onCompletion {
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
    base.launcherOnLifecycle(Dispatchers.Main) {
        lifecycle(this@lifecycle, base, showFailedView, errorCallback, callback)
    }
}

/**
 * 不带loading的lifecycle
 * @receiver Flow<T>
 * @param base BaseView
 * @param showFailedView Boolean 是否显示请求失败的页面
 * @param state Lifecycle.State 指定在哪个生命周期才回调数据(默认在前台才回调数据)
 * @param errorCallback Function1<[@kotlin.ParameterName] Throwable, Unit>? 异常的回调
 * @param callback [@kotlin.ExtensionFunctionType] Function1<T, Unit> 正常数据的回调
 */
fun <T> Flow<T>.repeatOnLifecycle(
    base: BaseView,
    showFailedView: Boolean = false,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    errorCallback: ((t: Throwable) -> Unit)? = null,
    callback: T.() -> Unit
) {
    base.repeatLauncherOnLifecycle(Dispatchers.Main, state) {
        lifecycle(this@repeatOnLifecycle, base, showFailedView, errorCallback, callback)
    }
}

private suspend fun <T> lifecycle(
    flow: Flow<T>,
    base: BaseView,
    showFailedView: Boolean = false,
    errorCallback: ((t: Throwable) -> Unit)? = null,
    callback: T.() -> Unit
) {
    flow.flowOn(Dispatchers.IO).onCompletion {
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

/**
 * 带loadingDialog的lifecycle
 * @receiver Flow<T>
 * @param base BaseView
 * @param showFailedView Boolean 是否显示请求失败的页面
 * @param errorCallback Function1<[@kotlin.ParameterName] Throwable, Unit>? 异常的回调
 * @param callback [@kotlin.ExtensionFunctionType] Function1<T, Unit> 正常数据的回调
 */
fun <T> Flow<T?>.lifecycleLoadingDialogNull(
    base: BaseView,
    showFailedView: Boolean = false,
    errorCallback: ((t: Throwable) -> Unit)? = null,
    callback: T?.() -> Unit
) {
    base.launcherOnLifecycle(Dispatchers.Main) {
        lifecycleLoadingDialogNull(
            this@lifecycleLoadingDialogNull,
            base,
            showFailedView,
            errorCallback,
            callback
        )
    }
}

/**
 * 带loadingDialog的lifecycle
 * @receiver Flow<T>
 * @param base BaseView
 * @param showFailedView Boolean 是否显示请求失败的页面
 * @param state Lifecycle.State 指定在哪个生命周期才回调数据(默认在前台才回调数据)
 * @param errorCallback Function1<[@kotlin.ParameterName] Throwable, Unit>? 异常的回调
 * @param callback [@kotlin.ExtensionFunctionType] Function1<T, Unit> 正常数据的回调
 */
fun <T> Flow<T?>.repeatOnLifecycleLoadingDialogNull(
    base: BaseView,
    showFailedView: Boolean = false,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    errorCallback: ((t: Throwable) -> Unit)? = null,
    callback: T?.() -> Unit
) {
    base.repeatLauncherOnLifecycle(Dispatchers.Main, state) {
        lifecycleLoadingDialogNull(
            this@repeatOnLifecycleLoadingDialogNull,
            base,
            showFailedView,
            errorCallback,
            callback
        )
    }
}

private suspend fun <T> lifecycleLoadingDialogNull(
    flow: Flow<T?>,
    base: BaseView,
    showFailedView: Boolean = false,
    errorCallback: ((t: Throwable) -> Unit)? = null,
    callback: T?.() -> Unit
) {
    flow.flowOn(Dispatchers.IO).onStart {
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
    base.launcherOnLifecycle(Dispatchers.Main) {
        lifecycleLoadingDialog(
            this@lifecycleLoadingDialog,
            base,
            showFailedView,
            errorCallback,
            callback
        )
    }
}

/**
 * 带loadingDialog的lifecycle
 * @receiver Flow<T>
 * @param base BaseView
 * @param showFailedView Boolean 是否显示请求失败的页面
 * @param state Lifecycle.State 指定在哪个生命周期才回调数据(默认在前台才回调数据)
 * @param errorCallback Function1<[@kotlin.ParameterName] Throwable, Unit>? 异常的回调
 * @param callback [@kotlin.ExtensionFunctionType] Function1<T, Unit> 正常数据的回调
 */
fun <T> Flow<T>.repeatOnLifecycleLoadingDialog(
    base: BaseView,
    showFailedView: Boolean = false,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    errorCallback: ((t: Throwable) -> Unit)? = null,
    callback: T.() -> Unit
) {
    base.repeatLauncherOnLifecycle(Dispatchers.Main, state) {
        lifecycleLoadingDialog(
            this@repeatOnLifecycleLoadingDialog,
            base,
            showFailedView,
            errorCallback,
            callback
        )
    }
}

private suspend fun <T> lifecycleLoadingDialog(
    flow: Flow<T>,
    base: BaseView,
    showFailedView: Boolean = false,
    errorCallback: ((t: Throwable) -> Unit)? = null,
    callback: T.() -> Unit
) {
    flow.flowOn(Dispatchers.IO).onStart {
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

/**
 * 带loadingView的lifecycle
 * @receiver Flow<T>
 * @param base BaseView
 * @param showFailedView Boolean 是否显示请求失败的页面
 * @param errorCallback Function1<[@kotlin.ParameterName] Throwable, Unit>? 异常的回调
 * @param callback [@kotlin.ExtensionFunctionType] Function1<T, Unit> 正常数据的回调
 */
fun <T> Flow<T?>.lifecycleLoadingViewNull(
    base: BaseView,
    showFailedView: Boolean = false,
    errorCallback: ((t: Throwable) -> Unit)? = null,
    callback: T?.() -> Unit
) {
    base.launcherOnLifecycle(Dispatchers.Main) {
        lifecycleLoadingViewNull(
            this@lifecycleLoadingViewNull,
            base,
            showFailedView,
            errorCallback,
            callback
        )
    }
}

/**
 * 带loadingView的lifecycle
 * @receiver Flow<T>
 * @param base BaseView
 * @param showFailedView Boolean 是否显示请求失败的页面
 * @param state Lifecycle.State 指定在哪个生命周期才回调数据(默认在前台才回调数据)
 * @param errorCallback Function1<[@kotlin.ParameterName] Throwable, Unit>? 异常的回调
 * @param callback [@kotlin.ExtensionFunctionType] Function1<T, Unit> 正常数据的回调
 */
fun <T> Flow<T?>.repeatOnLifecycleLoadingViewNull(
    base: BaseView,
    showFailedView: Boolean = false,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    errorCallback: ((t: Throwable) -> Unit)? = null,
    callback: T?.() -> Unit
) {
    base.repeatLauncherOnLifecycle(Dispatchers.Main, state) {
        lifecycleLoadingViewNull(
            this@repeatOnLifecycleLoadingViewNull,
            base,
            showFailedView,
            errorCallback,
            callback
        )
    }
}

private suspend fun <T> lifecycleLoadingViewNull(
    flow: Flow<T?>,
    base: BaseView,
    showFailedView: Boolean = false,
    errorCallback: ((t: Throwable) -> Unit)? = null,
    callback: T?.() -> Unit
) {
    flow.flowOn(Dispatchers.IO).onStart {
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
    base.launcherOnLifecycle(Dispatchers.Main) {
        lifecycleLoadingView(
            this@lifecycleLoadingView,
            base,
            showFailedView,
            errorCallback,
            callback
        )
    }
}

/**
 * 带loadingView的lifecycle
 * @receiver Flow<T>
 * @param base BaseView
 * @param showFailedView Boolean 是否显示请求失败的页面
 * @param state Lifecycle.State 指定在哪个生命周期才回调数据(默认在前台才回调数据)
 * @param errorCallback Function1<[@kotlin.ParameterName] Throwable, Unit>? 异常的回调
 * @param callback [@kotlin.ExtensionFunctionType] Function1<T, Unit> 正常数据的回调
 */
fun <T> Flow<T>.repeatOnLifecycleLoadingView(
    base: BaseView,
    showFailedView: Boolean = false,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    errorCallback: ((t: Throwable) -> Unit)? = null,
    callback: T.() -> Unit
) {
    base.repeatLauncherOnLifecycle(Dispatchers.Main, state) {
        lifecycleLoadingView(
            this@repeatOnLifecycleLoadingView,
            base,
            showFailedView,
            errorCallback,
            callback
        )
    }
}

private suspend fun <T> lifecycleLoadingView(
    flow: Flow<T>,
    base: BaseView,
    showFailedView: Boolean = false,
    errorCallback: ((t: Throwable) -> Unit)? = null,
    callback: T.() -> Unit
) {
    flow.flowOn(Dispatchers.IO).onStart {
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

/**
 * 不带loading的lifecycle
 * @receiver Flow<T>
 * @param baseViewModel BaseViewModel
 * @param showFailedView Boolean 是否显示请求失败的页面
 * @param errorCallback Function1<[@kotlin.ParameterName] Throwable, Unit>? 异常的回调
 * @param callback [@kotlin.ExtensionFunctionType] Function1<T, Unit> 正常数据的回调
 */
fun <T> Flow<T?>.lifecycleNull(
    baseViewModel: BaseViewModel,
    showFailedView: Boolean = false,
    errorCallback: ((t: Throwable) -> Unit)? = null,
    callback: T?.() -> Unit
) {
    baseViewModel.viewModelScope.launch(Dispatchers.Main) {
        this@lifecycleNull.flowOn(Dispatchers.IO).onCompletion {
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
fun <T> Flow<T?>.lifecycleLoadingViewNull(
    baseViewModel: BaseViewModel,
    showFailedView: Boolean = false,
    errorCallback: ((t: Throwable) -> Unit)? = null,
    callback: T?.() -> Unit
) {
    baseViewModel.viewModelScope.launch(Dispatchers.Main) {
        this@lifecycleLoadingViewNull.flowOn(Dispatchers.IO).onStart {
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
fun <T> Flow<T?>.lifecycleLoadingDialogNull(
    baseViewModel: BaseViewModel,
    showFailedView: Boolean = false,
    errorCallback: ((t: Throwable) -> Unit)? = null,
    callback: T?.() -> Unit
) {
    baseViewModel.viewModelScope.launch(Dispatchers.Main) {
        this@lifecycleLoadingDialogNull.flowOn(Dispatchers.IO).onStart {
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

