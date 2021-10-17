package com.catchpig.mvvm.apt.interfaces

import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

interface RecyclerAdapterCompiler {
    fun viewBanding(parent: ViewGroup): ViewBinding
}