# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/benbennest/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}


#############################################
#
# 对于一些基本指令的添加
#
#############################################
# 代码混淆压缩比，在0~7之间，默认为5，一般不做修改
-optimizationpasses 5

# 混合时不使用大小写混合，混合后的类名为小写
-dontusemixedcaseclassnames

# 指定不去忽略非公共库的类
-dontskipnonpubliclibraryclasses

# 这句话能够使我们的项目混淆后产生映射文件
# 包含有类名->混淆后类名的映射关系
-verbose

# 指定不去忽略非公共库的类成员
-dontskipnonpubliclibraryclassmembers

# 不做预校验，preverify是proguard的四个步骤之一，Android不需要preverify，去掉这一步能够加快混淆速度。
-dontpreverify

# 保留Annotation不混淆
-keepattributes *Annotation*,InnerClasses

# 避免混淆泛型
-keepattributes Signature

# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable

# 指定混淆是采用的算法，后面的参数是一个过滤器
# 这个过滤器是谷歌推荐的算法，一般不做更改
-optimizations !code/simplification/cast,!field/*,!class/merging/*


#############################################
#
# Android开发中一些需要保留的公共部分
#
#############################################

# 保留我们使用的四大组件，自定义的Application等等这些类不被混淆
# 因为这些子类都有可能被外部调用
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Appliction
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService


# 保留support下的所有类及其内部类
-keep class android.support.** {*;}

# 保留继承的
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.annotation.**

# 保留R下面的资源
-keep class **.R$* {*;}

# 保留本地native方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

# 保留在Activity中的方法参数是view的方法，
# 这样以来我们在layout中写的onClick就不会被影响
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}

# 保留枚举类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 保留我们自定义控件（继承自View）不被混淆
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 保留Parcelable序列化类不被混淆
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# 保留Serializable序列化的类不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# 对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆
-keepclassmembers class * {
    void *(**On*Event);
    void *(**On*Listener);
}

# webView处理，项目中没有使用到webView忽略即可
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
    public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, jav.lang.String);
}

# 移除Log类打印各个等级日志的代码，打正式包的时候可以做为禁log使用，这里可以作为禁止log打印的功能使用
# 记得proguard-android.txt中一定不要加-dontoptimize才起作用
# 另外的一种实现方案是通过BuildConfig.DEBUG的变量来控制
#-assumenosideeffects class android.util.Log {
#    public static int v(...);
#    public static int i(...);
#    public static int w(...);
#    public static int d(...);
#    public static int e(...);
#}

#############################################
#
# 项目中特殊处理部分
#
#############################################

#-----------处理反射类---------------



#-----------处理js交互---------------



#-----------处理实体类---------------



#-----------处理第三方依赖库---------











# for v4, v7
-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }

-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }

# for design
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.**{*;}
# for Annotation
-keep class * extends java.lang.annotation.Annotation

#butterknife proguard
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#retrofit proguard
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

##---------------Begin: proguard configuration for OkHttp and Okio  ----------
-keepattributes Signature
-keepattributes Annotation
# OkHttp
-dontwarn okhttp3.**
-keep class okhttp3.** { *; }
-keep class com.squareup.okhttp3.** {
*;
}

# Okio
-dontwarn okio.**
-keep class okio.** { *; }

# okhttp-urlconnection
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**
##---------------End: proguard configuration for OkHttp and Okio  ----------

# 友盟自动更新
-keepclassmembers class * { public (org.json.JSONObject); }
-keep public class cn.irains.parking.cloud.pub.R$*{ public static final int *; }
-keep public class * extends com.umeng.**
-keep class com.umeng.** { *; }
-dontwarn com.umeng.**


## For live cloud sdk
#-dontwarn com.qihoo.livecloud.**
#-dontwarn com.cv.faceapi.**
#-keep class com.qihoo.jia.** { *; }
#-keep class com.qihoo.livecloud.** { *; }
#

## For GreenDAO
#-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
#    public static java.lang.String TABLENAME;
#}
#-keep class **$Properties
#
##EventBus 插件内有引用 必须keep
#-keep class com.qihoo.lianxian.eventbus.QEventBus { *; }
#-keepclassmembers class ** {
#    public void onEvent*(**);
#}
#
## For tencent sdks
#-dontwarn com.tencent.**
#-keep class com.tencent.mm.sdk.openapi.WXMediaMessage { *;}
#-keep class com.tencent.mm.sdk.openapi.**{*;}
#-keep class com.tencent.mm.sdk.openapi.** implements com.tencent.mm.sdk.openapi.WXMediaMessage$IMediaObject {*;}
#-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage { *;}
#-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
#
## For sina weibo sdks
#-dontwarn com.sina.weibo.**
#-keep class com.sina.weibo.sdk.**{*;}
#-keep class com.sina.weibo.sdk.api.**{*;}
#-keep class com.sina.weibo.sdk.api.share.**{*;}
#-keep class com.sina.weibo.sdk.api.share.ui.**{*;}
#-keep class com.sina.weibo.sdk.auth.**{*;}
#-keep class com.sina.weibo.sdk.auth.sso.**{*;}
#-keep class com.sina.weibo.sdk.component.**{*;}
#-keep class com.sina.weibo.sdk.component.view.**{*;}
#-keep class com.sina.weibo.sdk.constant.**{*;}
#-keep class com.sina.weibo.sdk.exception.**{*;}
#-keep class com.sina.weibo.sdk.net.**{*;}
#-keep class com.sina.weibo.sdk.net.openapi.**{*;}
#-keep class com.sina.weibo.sdk.util.**{*;}
#-keep class com.qihoo.sdk.report.**{*;}
#
#-dontwarn com.qihoo.lianxian.share.**
#-keep class com.qihoo.lianxian.share.** {*;}
#
## Gson specific classes
#-keep class sun.misc.Unsafe { *; }
#
## Application classes that will be serialized/deserialized over Gson
#-keep class com.google.gson.examples.android.model.** { *; }
#
## For model/bean/db
#-keep class com.qihoo.lianxian.model.** {*;}
#-keep class com.qihoo.lianxian.bean.** {*;}
#-keep class com.qihoo.lianxian.db.** {*;}
#
## ForPlayerMode
#-keep class **$PlayerMode { *; }
#
##butterknife proguard
#-keep class butterknife.** { *; }
#-dontwarn butterknife.internal.**
#-keep class **$$ViewBinder { *; }
#
#-keepclasseswithmembernames class * {
#    @butterknife.* <fields>;
#}
#
#-keepclasseswithmembernames class * {
#    @butterknife.* <methods>;
#}
#
## for Webview js callbacks
#-keepclassmembers class * {
#    @android.webkit.JavascriptInterface <methods>;
#}
#
#-keepclassmembers class * extends android.webkit.WebChromeClient {
#    public void openFileChooser(...);
#}
#
#-keepclasseswithmembernames class * {
#native <methods>;
#}
#
#-keepclasseswithmembernames class * {
#public <init>(android.content.Context, android.util.AttributeSet);
#}
#
#-keepclasseswithmembernames class * {
#public <init>(android.content.Context, android.util.AttributeSet, int);
#}
#
#-keepclassmembers enum * {
#public static **[] values();
#public static ** valueOf(java.lang.String);
#}
#
#-keep class * implements android.os.Parcelable {
#public static final android.os.Parcelable$Creator *;
#}
#
#-dontwarn com.qihoo.lianxian.**
#
## gaode map
#-keep class com.amap.api.location.**{*;}
#
#-keep class com.amap.api.fence.**{*;}
#
#-keep class com.autonavi.aps.amapapi.model.**{*;}
#
#-keepclassmembers class * extends android.webkit.WebChromeClient {
#    public void openFileChooser(...);
#}
#
#
#-dontwarn com.facebook.stetho.**
#-dontwarn com.qihoo.kd.push.**
#-dontwarn com.qihoo.sdk.report.**
#-dontwarn com.fernandocejas.frodo.core.**
#
##tencent share
#-keep class com.tencent.open.TDialog$*
#-keep class com.tencent.open.TDialog$* {*;}
#-keep class com.tencent.open.PKDialog
#-keep class com.tencent.open.PKDialog {*;}
#-keep class com.tencent.open.PKDialog$*
#-keep class com.tencent.open.PKDialog$* {*;}
#
##openFileChooser
#-keepclassmembers class * {
#      public void openFileChooser(...);
#}
#
##Andfix
#-keep class com.alipay.euler.andfix.** { *; }