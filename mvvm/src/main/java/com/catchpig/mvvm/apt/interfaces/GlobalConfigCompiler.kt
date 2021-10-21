package com.catchpig.mvvm.apt.interfaces

import com.catchpig.mvvm.interfaces.IGlobalConfig

/**
 *
 * @author Li Tao
 * date 2021/10/21 10:57
 */
interface GlobalConfigCompiler {
    fun getGlobalConfig(): IGlobalConfig
}