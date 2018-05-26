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

##butterknife proguard
#-keep class butterknife.** { *; }
#-dontwarn butterknife.internal.**
#-keep class **$$ViewBinder { *; }
#-keepclasseswithmembernames class * {
#    @butterknife.* <fields>;
#}
#-keepclasseswithmembernames class * {
#    @butterknife.* <methods>;
#}
#
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
## for Annotation
#-keep class * extends java.lang.annotation.Annotation

# For glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
# for DexGuard only
-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keepattributes *Annotation*
-keepattributes Signature

-libraryjars libs/arm64-v8a/liblocSDK5.so
-libraryjars libs/armeabi/libbdpush_V2_2.so
-libraryjars libs/armeabi/liblocSDK5.so
-libraryjars libs/armeabi-v7a/liblocSDK5.so
-libraryjars libs/mips/libbdpush_V2_2.so
-libraryjars libs/mips64/liblocSDK5.so
-libraryjars libs/x86/liblocSDK5.so
-libraryjars libs/x86_64/liblocSDK5.so
-libraryjars libs/android-support-v4.jar
-libraryjars libs/baidumapapi_v2_4_1.jar
-libraryjars libs/ksoap2-android-assembly-3.0.0-jar-with-dependencies.jar
-libraryjars libs/locSDK_5.0.jar
-libraryjars libs/pushservice-4.2.0.63.jar
-libraryjars libs/apache-mime4j-0.6.jar
-libraryjars libs/injectlib.jar


-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.v4.**
-keep public class com.android.vending.licensing.ILicensingService

-dontwarn android.support.v4.**
-dontwarn com.baidu.**

-keepnames class * implements java.io.Serializable
-keep public class * implements java.io.Serializable {
	public *;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *; }
-libraryjars libs/fastjson-1.2.3.jar

-dontwarn org.apache.http.entity.mime.**
-keep class org.apache.http.entity.mime.** { *; }
-libraryjars libs/httpmime-4.0.jar

-dontwarn org.apache.james.mime4j.**
-keep class org.apache.james.mime4j.** {*;}

-keep class com.baidu.** { *; }
-keep class vi.com.gdi.bgl.android.**{ *; }
-keep class com.qinhan.library_3_0.Model.** { *; }
-keep public class javax.**
-keep public class android.webkit.**

-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context);
}
-dontshrink
-dontoptimize
-dontwarn android.webkit.WebView
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

-keep public class [your_pkg].R$*{
    public static final int *;
}
