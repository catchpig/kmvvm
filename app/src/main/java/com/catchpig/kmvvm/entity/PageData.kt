package com.catchpig.kmvvm.entity

import kotlinx.serialization.Serializable

@Serializable
data class PageData<T>(val curPage: Int, val datas: MutableList<T>)
