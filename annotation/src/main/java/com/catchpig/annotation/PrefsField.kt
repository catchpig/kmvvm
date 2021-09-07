package com.catchpig.annotation

/**
 * Shared的参数注解
 * 只能修饰在Double,Float,Int,Long,String,Boolean类型上的注解
 * @author catchpig
 * @date 2019/10/29 00:29
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class PrefsField (
        /**
         * 字段名字,如果为空就取参数名称
         */
        val value:String = ""
)