package com.catchpig.mvvm.ext

import androidx.lifecycle.lifecycleScope
import com.catchpig.mvvm.apt.KotlinMvvmCompiler
import com.catchpig.mvvm.base.activity.BaseVMActivity
import com.catchpig.mvvm.base.fragment.BaseVMFragment
import com.catchpig.mvvm.widget.refresh.RefreshRecyclerView
import com.catchpig.utils.ext.logd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private const val TAG = "FlowExt"
fun <T> Flow<MutableList<T>>.lifecycleRefresh(
    base: BaseVMFragment<*, *>, refreshLayoutWrapper: RefreshRecyclerView
) {
    base.lifecycleScope.launch(Dispatchers.Main) {
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
    base: BaseVMFragment<*, *>,
    errorCallback: ((t: Throwable) -> Unit)? = null,
    callback: T.() -> Unit
) {
    base.lifecycleScope.launch(Dispatchers.Main) {
        this@lifecycle.flowOn(Dispatchers.IO).onCompletion {
            "$TAG:lifecycle:onCompletion".logd(base::class.simpleName!!)
        }.catch { t ->
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
    base: BaseVMFragment<*, *>,
    errorCallback: ((t: Throwable) -> Unit)? = null,
    callback: T.() -> Unit
) {
    base.lifecycleScope.launch(Dispatchers.Main) {
        this@lifecycleLoadingDialog.flowOn(Dispatchers.IO).onStart {
            base.loadingDialog()
        }.onCompletion {
            "$TAG:lifecycleLoadingDialog:onCompletion".logd(base::class.simpleName!!)
            base.hideLoading()
        }.catch { t ->
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
    base: BaseVMFragment<*, *>,
    errorCallback: ((t: Throwable) -> Unit)? = null,
    callback: T.() -> Unit
) {
    base.lifecycleScope.launch(Dispatchers.Main) {
        this@lifecycleLoadingView.flowOn(Dispatchers.IO).onStart {
            base.loadingView()
        }.onCompletion {
            "$TAG:lifecycleLoadingView:onCompletion".logd(base::class.simpleName!!)
            base.hideLoading()
        }.catch { t ->
            KotlinMvvmCompiler.onError(base, t)
            errorCallback?.let {
                errorCallback(t)
            }
        }.collect {
            callback(it)
        }
    }
}

fun <T> Flow<MutableList<T>>.lifecycleRefresh(
    base: BaseVMActivity<*, *>, refreshLayoutWrapper: RefreshRecyclerView
) {
    base.lifecycleScope.launch(Dispatchers.Main) {
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
    base: BaseVMActivity<*, *>,
    errorCallback: ((t: Throwable) -> Unit)? = null,
    callback: T.() -> Unit
) {
    base.lifecycleScope.launch(Dispatchers.Main) {
        this@lifecycle.flowOn(Dispatchers.IO).onCompletion {
            "$TAG:lifecycle:onCompletion".logd(base::class.simpleName!!)
        }.catch { t ->
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
    base: BaseVMActivity<*, *>,
    errorCallback: ((t: Throwable) -> Unit)? = null,
    callback: T.() -> Unit
) {
    base.lifecycleScope.launch(Dispatchers.Main) {
        this@lifecycleLoadingDialog.flowOn(Dispatchers.IO).onStart {
            base.loadingDialog()
        }.onCompletion {
            "$TAG:lifecycleLoadingDialog:onCompletion".logd(base::class.simpleName!!)
            base.hideLoading()
        }.catch { t ->
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
    base: BaseVMActivity<*, *>,
    errorCallback: ((t: Throwable) -> Unit)? = null,
    callback: T.() -> Unit
) {
    base.lifecycleScope.launch(Dispatchers.Main) {
        this@lifecycleLoadingView.flowOn(Dispatchers.IO).onStart {
            base.loadingView()
        }.onCompletion {
            "$TAG:lifecycleLoadingView:onCompletion".logd(base::class.simpleName!!)
            base.hideLoading()
        }.catch { t ->
            KotlinMvvmCompiler.onError(base, t)
            errorCallback?.let {
                errorCallback(t)
            }
        }.collect {
            callback(it)
        }
    }
}