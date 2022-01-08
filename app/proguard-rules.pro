# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#-keep class com.catchpig.mvvm.**
#-keep class com.catchpig.utils.**
-keep class com.catchpig.annotation.enums.**
#-keep class com.catchpig.loading.**

-keep class com.google.android.material.snackbar.Snackbar {*;}

-keep @com.catchpig.annotation.Adapter class * {*;}
-keep @com.catchpig.annotation.ServiceApi class * {*;}
#-keep public class * extends com.catchpig.mvvm.base.activity.BaseVMActivity
#-keep public class * extends com.catchpig.mvvm.base.activity.BaseActivity
#-keep public class * extends com.catchpig.mvvm.base.fragment.BaseFragment
#-keep public class * extends com.catchpig.mvvm.base.fragment.BaseVMFragment

-keep public class **.databinding.*Binding {*;}

-keep class **.*_Compiler {*;}

-keep class com.gyf.immersionbar.* {*;}
-dontwarn com.gyf.immersionbar.**

#序列化混淆
-if @kotlinx.serialization.Serializable class **
-keepclassmembers class <1> {
    static <1>$Companion Companion;
}

# Keep `serializer()` on companion objects (both default and named) of serializable classes.
-if @kotlinx.serialization.Serializable class ** {
    static **$* *;
}
-keepclassmembers class <1>$<3> {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep `INSTANCE.serializer()` of serializable objects.
-if @kotlinx.serialization.Serializable class ** {
    public static ** INSTANCE;
}
-keepclassmembers class <1> {
    public static <1> INSTANCE;
    kotlinx.serialization.KSerializer serializer(...);
}

# @Serializable and @Polymorphic are used at runtime for polymorphic serialization.
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault