package com.catchpig.kotlin_mvvm.mvvm.transparent

import androidx.lifecycle.MutableLiveData
import com.catchpig.kotlin_mvvm.entity.Banner
import com.catchpig.kotlin_mvvm.repository.WanAndroidRepository
import com.catchpig.mvvm.base.viewmodel.BaseViewModel
import com.catchpig.mvvm.ext.success

class TransparentViewModel : BaseViewModel() {
    private val wanAndroidProxy = WanAndroidRepository
    val bannerLiveData = MutableLiveData<Banner>()

    fun getBanner() {
        wanAndroidProxy.getBanners().success({
            bannerLiveData.value = it
        })
    }
}