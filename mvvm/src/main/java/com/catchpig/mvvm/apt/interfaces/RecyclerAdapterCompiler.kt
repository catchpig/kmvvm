package com.catchpig.mvvm.apt.interfaces

import android.view.ViewGroup
import com.catchpig.mvvm.entity.AdapterBinding

interface RecyclerAdapterCompiler {
    fun viewBanding(parent: ViewGroup): AdapterBinding
}