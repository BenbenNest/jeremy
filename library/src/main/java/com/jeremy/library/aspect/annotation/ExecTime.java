/**
 * Copyright (C) 2018 com.jeremy.library All rights reserved.
 *
 * @author jeremy (changqing.zhao)
 */
package com.jeremy.library.aspect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Calculate Total execution time of Method
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD})
public @interface ExecTime
{
}
