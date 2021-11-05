package com.catchpig.kmvvm.transparent

import androidx.lifecycle.MutableLiveData
import com.catchpig.kmvvm.entity.Banner
import com.catchpig.kmvvm.repository.WanAndroidRepository
import com.catchpig.mvvm.base.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class TransparentViewModel : BaseViewModel() {
    private val wanAndroidProxy = WanAndroidRepository
    val bannerLiveData = MutableLiveData<Banner>()

    suspend fun banner(): Flow<Banner> {
        return flowOf(wanAndroidProxy.queryBanner())
    }
}