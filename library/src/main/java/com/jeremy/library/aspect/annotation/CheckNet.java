package com.jeremy.library.aspect.annotation;

/**
 * Created by changqing on 2018/5/29.
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 检查网络
 * 如果需要在运行时获取Annotation，需要设置为@Retention(RetentionPolicy.RUNTIME)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface CheckNet
{
}

