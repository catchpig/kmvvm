package com.catchpig.aop

import com.catchpig.annotation.TimeLog
import com.catchpig.utils.ext.log
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut

/**
 * 方法和构造函数的耗时打印
 * @author catchpig
 * @date 2019/10/20 00:20
 */
@Aspect
class TimeLogAspectJ {
    companion object{
        const val TAG = "[TimeLog]"
    }

    @Pointcut("execution(@com.catchpig.annotation.TimeLog * *(..))")
    fun methodTime(){

    }

    @Pointcut("execution(@com.catchpig.annotation.TimeLog *.new(..))")
    fun constructorTime(){

    }

    @Around("(methodTime() || constructorTime()) && @annotation(timeLog)")
    fun clickGap(proceedingJoinPoint: ProceedingJoinPoint,timeLog: TimeLog){
        val beforeTimeOfMethod = System.currentTimeMillis()
        proceedingJoinPoint.proceed()
        val afterTimeOfMethod = System.currentTimeMillis()
        val className = proceedingJoinPoint.target::class.java.simpleName
        val method = proceedingJoinPoint.signature.name
        var params = StringBuffer("")
        proceedingJoinPoint.args.forEach {
            if (it != null) {
                params.append(",${it.javaClass.simpleName}")
            }else{
                params.append(",null")
            }
        }
        params.delete(0,1)
        val time = afterTimeOfMethod-beforeTimeOfMethod
        val level = timeLog.value
        log(level,TAG,"${className}.${method}(${params})耗时:${time}毫秒")
    }
}