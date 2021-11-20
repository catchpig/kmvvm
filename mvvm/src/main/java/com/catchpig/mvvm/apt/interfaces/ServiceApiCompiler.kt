package com.catchpig.mvvm.apt.interfaces

import com.catchpig.mvvm.entity.ServiceParam

interface ServiceApiCompiler {
    fun getServiceParam(className: String): ServiceParam
}