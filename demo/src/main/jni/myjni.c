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
    /* 根据各编译器平台内部变量来区分cmake使用的是哪种编译器 */
#if defined(__arm__)
#if defined(__ARM_ARCH_7A__)
#if defined(__ARM_NEON__)
    #if defined(__ARM_PCS_VFP)
        #define ABI "armeabi-v7a/NEON (hard-float)"
      #else
        #define ABI "armeabi-v7a/NEON"
      #endif
#else
#if defined(__ARM_PCS_VFP)
#define ABI "armeabi-v7a (hard-float)"
#else
#define ABI "armeabi-v7a"
#endif
#endif
#else
#define ABI "armeabi"
#endif
#elif defined(__i386__)
    #define ABI "x86"
#elif defined(__x86_64__)
#define ABI "x86_64"
#elif defined(__mips64)  /* mips64el-* toolchain defines __mips__ too */
#define ABI "mips64"
#elif defined(__mips__)
#define ABI "mips"
#elif defined(__aarch64__)
#define ABI "arm64-v8a"
#else
#define ABI "unknown"
#endif

    return (*env)->NewStringUTF(env, "Hello JNI !  This Mobile CPU Arch is " ABI ".\n");
}

#ifdef __cplusplus
}
#endif
#endif