//
// Created by zhaochangqing on 2018/10/10.
//

#include <jni.h>
#include <android/log.h>
#include <string.h>

#ifndef _Included_com_jeremy_demo_jni_MyJNI // 1
#define _Included_com_jeremy_demo_jni_MyJNI

#ifdef __cplusplus // 2
extern "C" {
#endif // 2
JNIEXPORT jstring JNICALL Java_com_jeremy_demo_jni_MyJNI_helloworld(JNIEnv *, jobject);
#ifdef __cplusplus // 3
}
#endif // 3
#endif // 1

JNIEXPORT jstring JNICALL Java_com_jeremy_demo_jni_MyJNI_helloworld(JNIEnv * env,
        jobject obj) {
    /* JNIEnv* 是指向虚拟机的指针，jobject 是指向从 Java 端传递的隐式 this 对象的指针。*/
    return env->NewStringUTF("This is another cpp file.");
}