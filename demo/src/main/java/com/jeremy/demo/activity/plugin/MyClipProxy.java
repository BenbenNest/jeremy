package com.jeremy.demo.activity.plugin;

import android.os.IBinder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by changqing on 2017/12/24.
 */

public class MyClipProxy implements InvocationHandler {
    private final IBinder mBase;

    public MyClipProxy(IBinder binder) {
        mBase = binder;//这里传入的是原系统的代理类
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //拦截原系统类查询本地是否有这个代理的方法
        if ("queryLocalInterface".equals(method.getName())) {
            //我们这里要创建我们自己的系统类，然后返回
            //1.拿到系统的aidl类中的stub，因为这个对象本来就是个代理,而且源码执行了
//            static private IClipboard getService() {
//                synchronized (sStaticLock) {
//                    if (sService != null) {
//                        return sService;
//                    }
//                    IBinder b = ServiceManager.getService("clipboard");
//                    sService = IClipboard.Stub.asInterface(b);
//                    return sService;
//                }
//            }
            Class<?> mStubClass = Class.forName("android.content.IClipboard$Stub");
            //2.在拿到IClipboard本地对象类
            Class<?> mIClipboard = Class.forName("android.content.IClipboard");
            //3.创建我们自己的代理
            return Proxy.newProxyInstance(mStubClass.getClassLoader(),
                    new Class[]{mIClipboard},
                    new MyClip(mBase, mStubClass));
        }
        //不是这个方法还是返回原系统的执行
        return method.invoke(mBase, args);
    }
}
