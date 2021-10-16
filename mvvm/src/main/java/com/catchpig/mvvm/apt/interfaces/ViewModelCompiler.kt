package com.catchpig.mvvm.apt.interfaces

import com.catchpig.mvvm.base.viewmodel.BaseViewModel

interface ViewModelCompiler {
    fun onError(baseViewModel: BaseViewModel, t: Throwable)
}