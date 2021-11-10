package com.catchpig.kmvvm.entity

data class PageData<T>(val curPage: Int, val datas: MutableList<T>)
