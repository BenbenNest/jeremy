// IMyAIDLInterface.aidl
package com.jeremy.demos;

// Declare any non-default types here with import statements

interface IMyAIDLInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);

    int add(in int val1,in int val2);

    int minus(in int val1,in int val2);

}
