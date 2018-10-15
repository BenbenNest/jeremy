package com.jeremy.demo.jni;

public class HelloWorld {
    static {
        System.loadLibrary("myjni");
    }

    public native String helloworld();
}
