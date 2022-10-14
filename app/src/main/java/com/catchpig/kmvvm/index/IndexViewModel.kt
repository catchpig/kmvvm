package com.catchpig.kmvvm.index

import com.catchpig.kmvvm.entity.Banner
import com.catchpig.kmvvm.exception.HttpServerException
import com.catchpig.kmvvm.repository.WanAndroidRepository
import com.catchpig.mvvm.base.viewmodel.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class IndexViewModel : BaseViewModel() {
    fun queryBanners(): Flow<MutableList<Banner>> {
        return WanAndroidRepository.getBanners()
    }

    /**
     * 处理异常
     * @return Flow<Any>
     */
    fun handlerError(): Flow<Any> {
        return flowOf("").map {
            delay(2000)
            throw HttpServerException("001", "异常捕获")
        }
    }
}