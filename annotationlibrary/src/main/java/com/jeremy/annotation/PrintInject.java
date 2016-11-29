package com.jeremy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by benben on 2016/09/20.
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
public @interface PrintInject {

}
//@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
//@Retention(RetentionPolicy.CLASS)
//public @interface PrintInject {
//    int[] value();
//}
