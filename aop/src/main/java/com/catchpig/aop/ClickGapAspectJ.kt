package com.catchpig.aop

import com.catchpig.annotation.ClickGap
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut

/**
 * 防止重复点击
 * @author catchpig
 * @date 2019/8/19 00:19
 */
@Aspect
class ClickGapAspectJ {
    private var lastTimeMillis:Long = 0
    @Pointcut("execution(@com.catchpig.annotation.ClickGap * *(..))")
    fun pointcut(){

    }

    @Around("pointcut() && @annotation(clickGap)")
    fun clickGap(proceedingJoinPoint:ProceedingJoinPoint,clickGap: ClickGap){
        val currentTimeMillis = System.currentTimeMillis()
        if((currentTimeMillis-lastTimeMillis) > clickGap.value){
            proceedingJoinPoint.proceed()
        }
        lastTimeMillis = currentTimeMillis
    }
}