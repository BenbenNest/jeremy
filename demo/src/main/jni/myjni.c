//
// Created by zhaochangqing on 2017/12/29.
//
#include <jni.h>
/* Header for class com_jeremy_demo_jni_MyJNI */

#ifndef _Included_com_jeremy_demo_jni_MyJNI
#define _Included_com_jeremy_demo_jni_MyJNI
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_jeremy_demo_jni_MyJNI
 * Method:    getStringFromC
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_jeremy_demo_jni_MyJNI_getStringFromC(JNIEnv *env, jclass jobj){
    return (*env)->NewStringUTF(env,"Jeremy MyFirst JNI!");
}

#ifdef __cplusplus
}
#endif
#endif