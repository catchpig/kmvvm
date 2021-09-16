package com.catchpig.mvvm.base.callback

import com.catchpig.utils.ext.loge
import io.reactivex.rxjava3.subscribers.ResourceSubscriber

/**
 * @author litao
 * date 2021/9/11
 * description:
 */
abstract class BaseCallback<T>(
    var iLoadingDialog: ILoadingDialog? = null,
    var isLoadingDialog: Boolean = true,
    var throwError: Boolean = true
) :
    ResourceSubscriber<T>() {
    companion object {
        private const val TAG = "BaseCallback"
    }

    override fun onStart() {
        super.onStart()
        iLoadingDialog?.let {
            it.showLoading(isLoadingDialog)
        }
    }

    override fun onNext(t: T) {
        onSuccess(t)
    }

    abstract fun onSuccess(t: T)

    override fun onError(t: Throwable?) {
        t?.let {
            if (throwError) {
                iLoadingDialog?.run {
                    onError(it)
                }
            }
            it.message?.run {
                loge(TAG)
            }
        }
        hideLoading()
    }

    override fun onComplete() {
        hideLoading()
    }

    private fun hideLoading() {
        iLoadingDialog?.let {
            it.hideLoading()
        }
    }

    interface ILoadingDialog {
        /**
         * 展示Dialog
         */
        fun showLoading(isLoadingDialog: Boolean)

        /**
         * 隐藏Dialog
         */
        fun hideLoading()

        fun onError(t: Throwable);
    }
}