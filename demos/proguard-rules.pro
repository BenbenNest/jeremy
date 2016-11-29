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







#-optimizationpasses 5
#-dontusemixedcaseclassnames
#-dontskipnonpubliclibraryclasses
#-dontpreverify
#-dontnote ***
#-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod
#
## For retrolambda
#-dontwarn java.lang.invoke.*
#
## For retrofit
#-dontwarn retrofit2.**
#-keep class retrofit2.** { *; }
#
## For butterknife
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
## For rxjava
#-dontwarn javax.annotation.**
#-dontwarn javax.inject.**
#-dontwarn sun.misc.Unsafe
#-dontwarn org.mockito.**
#-dontwarn org.junit.**
#-dontwarn org.robolectric.**
#-keep class rx.internal.util.unsafe.** { *; }
#
## For okio
#-dontwarn okio.**
#
## For live cloud sdk
#-dontwarn com.qihoo.livecloud.**
#-dontwarn com.cv.faceapi.**
#-keep class com.qihoo.jia.** { *; }
#-keep class com.qihoo.livecloud.** { *; }
#
## For glide
#-keep public class * implements com.bumptech.glide.module.GlideModule
#-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
#  **[] $VALUES;
#  public *;
#}
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
## for v4, v7
#-dontwarn android.support.v4.**
#-keep class android.support.v4.** { *; }
#-keep interface android.support.v4.** { *; }
#
#-dontwarn android.support.v7.**
#-keep class android.support.v7.** { *; }
#-keep interface android.support.v7.** { *; }
#
## for design
#-dontwarn android.support.design.**
#-keep class android.support.design.** { *; }
#-keep interface android.support.design.**{*;}
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
#-keep class * extends java.lang.annotation.Annotation
#-keep class com.alipay.euler.andfix.** { *; }



