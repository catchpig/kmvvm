package com.catchpig.kmvvm.transparent

import androidx.lifecycle.MutableLiveData
import com.catchpig.kmvvm.entity.Banner
import com.catchpig.kmvvm.repository.WanAndroidRepository
import com.catchpig.mvvm.base.viewmodel.BaseViewModel
import com.catchpig.mvvm.ext.lifecycle
import com.catchpig.mvvm.ext.lifecycleLoadingDialog
import com.catchpig.mvvm.ext.lifecycleLoadingView

class TransparentViewModel : BaseViewModel() {
    val liveData = MutableLiveData<Banner>()

    override fun onCreate() {
        banner()
    }
    fun banner() {
        WanAndroidRepository.queryBanner().lifecycleLoadingView(this) {
            liveData.value = this
        }
//        WanAndroidRepository.queryBanner().lifecycleLoadingDialog(this) {
//            liveData.value = this
//        }
//        WanAndroidRepository.queryBanner().lifecycle(this) {
//            liveData.value = this
//        }
    }
}