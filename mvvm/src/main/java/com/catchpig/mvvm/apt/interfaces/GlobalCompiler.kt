package com.catchpig.mvvm.apt.interfaces

import android.app.Activity
import com.catchpig.mvvm.entity.ServiceParam
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
     * 发送Rxjava或者Flow的异常到被注解FlowError修饰的接口实现类
     * @param any Any
     * @param t Throwable
     */
    fun onError(any: Any, t: Throwable)


}