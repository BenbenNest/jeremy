package com.jeremy.library.utils;

import java.lang.reflect.Constructor;

/**
 * Created by changqing on 2018/4/9.
 */

public class ClassUtils {

    public void getAndPrintConstructors(Class cls) {
        Class<?> c1 = cls;
        Constructor<?>[] cons = c1.getConstructors();
        printConstructor(cons);
    }


    private static void printConstructor(Constructor<?>[] cons) {
        for (int i = 0; i < cons.length; i++) {
            System.out.println(cons[i]);
        }
    }

    public static void test() {
        try {
            Constructor c = C.class.getDeclaredConstructor(int.class);
            c.setAccessible(true);
            c.newInstance(5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

class C {
    public C() {
    }

    private C(int i) {
        System.out.println("HelloC" + i);
    }

    {
        System.out.println("I'm C class");
    }

    static {
        System.out.println("static C");
    }
}
