/**
 * Copyright (C) 2018 com.jeremy. All rights reserved.
 *
 * @author jeremy (changqing.zhao)
 */
package com.jeremy.library.aspect.aspect;

import android.util.Log;

import com.jeremy.library.aspect.internal.StopWatch;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Aspect处理
 */
@Aspect
public class ExecTimeAspect {

    private static final String POINTCUT_METHOD =
            "execution(@com.jeremy.library.aspect.annotation.ExecTime * *(..))";

    private static final String POINTCUT_CONSTRUCTOR =
            "execution(@com.jeremy.library.aspect.annotation.ExecTime *.new(..))";

    @Pointcut(POINTCUT_METHOD)
    public void methodAnnotatedWithExecTime() {
    }

    @Pointcut(POINTCUT_CONSTRUCTOR)
    public void constructorAnnotatedExecTime() {
    }

//    @Around("methodAnnotatedWithExecTime() || constructorAnnotatedExecTime()")
    @Around("methodAnnotatedWithExecTime()")
    public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result = joinPoint.proceed();
        stopWatch.stop();
        long timeConsume=stopWatch.getTotalTimeMillis();
//        if(timeConsume>1000)//当方法耗时大于某个值时打印Log
        Log.w(className, buildLogMessage(methodName, timeConsume));
        return result;
    }

    /**
     * Create a log message.
     *
     * @param methodName     A string with the method name.
     * @param methodDuration Duration of the method in milliseconds.
     * @return A string representing message.
     */
    private static String buildLogMessage(String methodName, long methodDuration) {
        StringBuilder message = new StringBuilder();
        message.append("ExecTime --> ");
        message.append(methodName);
        message.append(" --> ");
        message.append("[");
        message.append(methodDuration);
        message.append("ms");
        message.append("]");
        return message.toString();
    }
}
