APP_PLATFORM := android-14

LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
#编译生成的文件的类库叫什么名字
LOCAL_MODULE    := myjni
#要编译的c文件
LOCAL_SRC_FILES := myjni.c \
                   helloworld.cpp
include $(BUILD_SHARED_LIBRARY)

APP_ABI := armeabi armeabi-v7a