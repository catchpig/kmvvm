package com.catchpig.kmvvm.index

import com.catchpig.kmvvm.entity.Banner
import com.catchpig.kmvvm.repository.WanAndroidRepository
import com.catchpig.mvvm.base.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class IndexViewModel : BaseViewModel() {
    fun queryBanners(): Flow<MutableList<Banner>> {
        return WanAndroidRepository.getBanners()
    }
}