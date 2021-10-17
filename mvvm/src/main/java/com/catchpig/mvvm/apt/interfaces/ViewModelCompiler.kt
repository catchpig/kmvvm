package com.catchpig.mvvm.apt.interfaces

import com.catchpig.mvvm.base.viewmodel.IBaseViewModel

interface ViewModelCompiler {
    fun onError(iBaseViewModel: IBaseViewModel, t: Throwable)
}