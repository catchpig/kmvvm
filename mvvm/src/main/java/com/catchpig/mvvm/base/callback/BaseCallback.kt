package com.catchpig.mvvm.base.callback

import com.catchpig.mvvm.base.viewmodel.BaseViewModel
import com.catchpig.utils.ext.loge
import io.reactivex.rxjava3.subscribers.ResourceSubscriber

/**
 * @author litao
 * date 2021/9/11
 * description:
 */
abstract class BaseCallback<T>(
    var baseViewModel: BaseViewModel? = null,
    var isLoadingDialog: Boolean = true
) :
    ResourceSubscriber<T>() {
    companion object {
        private const val TAG = "BaseCallback"
    }

    override fun onStart() {
        super.onStart()
        baseViewModel?.let {
            it.showLoading(isLoadingDialog)
        }
    }

    override fun onNext(t: T) {
        onSuccess(t)
    }

    abstract fun onSuccess(t: T)

    override fun onError(t: Throwable?) {
        t?.let { it ->
            it.message?.let {
                it.loge(TAG)
            }
        }
        hideLoading()
    }

    override fun onComplete() {
        hideLoading()
    }

    private fun hideLoading() {
        baseViewModel?.let {
            it.hideLoading()
        }
    }
}