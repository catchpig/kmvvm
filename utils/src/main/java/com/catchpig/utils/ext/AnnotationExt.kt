package com.catchpig.utils.ext

/**
 * 只在当前类中获取注解类
 */
inline fun <reified A : Annotation> Any.annotation():A?{
    val cls = this::class.java
    val returnCls = A::class.java
    return if(cls.isAnnotationPresent(returnCls)){
        cls.getAnnotation(returnCls)
    }else{
        null
    }
}