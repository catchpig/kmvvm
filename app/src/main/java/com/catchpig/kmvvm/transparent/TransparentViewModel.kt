package com.catchpig.kmvvm.transparent

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.catchpig.kmvvm.entity.Banner
import com.catchpig.kmvvm.repository.WanAndroidRepository
import com.catchpig.mvvm.base.viewmodel.BaseViewModel
import com.catchpig.mvvm.ext.lifecycle

class TransparentViewModel : BaseViewModel() {
    val liveData = MutableLiveData<Banner>()

    override fun onCreate() {
        banner()
    }
    fun banner() {
        WanAndroidRepository.queryBanner().lifecycle(viewModelScope) {
            liveData.value = this
        }
    }
}