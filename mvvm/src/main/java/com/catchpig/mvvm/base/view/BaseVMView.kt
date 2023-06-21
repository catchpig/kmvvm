package com.catchpig.mvvm.base.view

interface BaseVMView : BaseView {
    fun initParam()

    fun initView()

    fun initFlow()
}