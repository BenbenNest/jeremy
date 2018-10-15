package com.jeremy.demo;

import com.jeremy.demo.jni.MyJNI;

import org.junit.Test;

/**
 * Created by changqing on 2018/8/31.
 */

public class TempTest {

    @Test
    public void testDeadLock() {
        String src = "testDeadLock";
        String result = src.replace("testf","testk");
        System.out.print("jni:" + MyJNI.getStringFromC());
        System.out.print("jni:" +MyJNI.helloworld());
    }
}
