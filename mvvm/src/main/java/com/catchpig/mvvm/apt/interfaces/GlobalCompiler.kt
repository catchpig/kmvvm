package com.catchpig.mvvm.apt.interfaces

import com.catchpig.mvvm.base.viewmodel.IBaseViewModel
import com.catchpig.mvvm.interfaces.IGlobalConfig

/**
 *
 * @author Li Tao
 * date 2021/10/21 10:57
 */
interface GlobalCompiler {
    /**
     * 获取全局配置参数
     * @return IGlobalConfig
     */
    fun getGlobalConfig(): IGlobalConfig

    /**
     * 发送Rxjava的异常到被注解ObserverError修饰的接口实现类
     * @param iBaseViewModel IBaseViewModel
     * @param t Throwable
     */
    fun onError(iBaseViewModel: IBaseViewModel, t: Throwable)
}