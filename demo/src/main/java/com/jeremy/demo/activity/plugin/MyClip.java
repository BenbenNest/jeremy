package com.jeremy.demo.activity.plugin;

import android.content.ClipData;
import android.os.IBinder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by changqing on 2017/12/24.
 */

public class MyClip implements InvocationHandler {
    private Object mBase;

    public MyClip(IBinder base, Class stub) {
        //拿到asInteface方法，因为源码中执行了这一句，我们也要执行这一句
        try {
            Method asInterface = stub.getDeclaredMethod("asInterface", IBinder.class);
            mBase = asInterface.invoke(null, base);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //这里我们拦截粘贴的方法，
        if ("getPrimaryClip".equals(method.getName())) {
            return ClipData.newPlainText(null, "我是jeremy，我改了系统源码，哈哈哈");
        }
        //再拦截是否有复制的方法，放系统认为一直都有
        if ("hasPrimaryClip".equals(method.getName())) {
            return true;
        }
        //其他启动还是返回原有的
        return method.invoke(mBase, args);
    }
}
