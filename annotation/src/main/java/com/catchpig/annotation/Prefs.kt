package com.catchpig.annotation

import com.catchpig.annotation.enums.PrefsMode

/**
 * SharedPreferences注解,可生成对应的文件
 * @author catchpig
 * @date 2019/10/29 00:29
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class Prefs (
        /**
         * 别名,如果为空则取类名
         */
        val value:String = "",
        /**
         * SharedPreferences模式
         */
        val mode:PrefsMode = PrefsMode.MODE_PRIVATE
)