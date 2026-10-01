# ============================================================
# 爱Ai (AiAi) ProGuard / R8 Rules
# ============================================================

# === 通用优化 ===
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-allowaccessmodification
-repackageclasses 'com.aiai.app.internal'

# === 保留注解 ===
-keepattributes *Annotation*
-keepattributes RuntimeVisibleAnnotations
-keepattributes RuntimeInvisibleAnnotations
-keepattributes Signature
-keepattributes InnerClasses
-keepattributes EnclosingMethod
-keepattributes SourceFile,LineNumberTable
-keepattributes Exceptions

# === 保留Model/Data类（Gson序列化） ===
-keep class com.aiai.network.model.** { *; }
-keep class com.aiai.data.entity.** { *; }
-keep class com.aiai.data.model.** { *; }
-keep class com.aiai.app.model.** { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
-keep class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# === Gson ===
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# === Retrofit ===
-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-keepattributes Exceptions

# === OkHttp ===
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-keep class okio.** { *; }

# === OkHttp SSE ===
-dontwarn okhttp3.sse.**

# === Hilt / Dagger ===
-keep class dagger.hilt.** { *; }
-keep class * extends dagger.hilt.android.HiltAndroidApp
-keep class * extends dagger.hilt.android.AndroidEntryPoint
-keep @dagger.hilt.android.HiltAndroidApp class *
-keep @dagger.hilt.android.AndroidEntryPoint class *
-keep class * extends dagger.hilt.InstallIn
-keep class **_HiltModules* { *; }
-keep class * implements dagger.hilt.internal.GeneratedComponent
-keep class * implements dagger.hilt.android.internal.lifecycle.HiltViewModelFactory$ViewModelComponent
-keep class dagger.hilt.android.lifecycle.HiltViewModel
-keep class * extends androidx.lifecycle.ViewModel { *; }
-keepclassmembers class * {
    @dagger.hilt.android.lifecycle.HiltViewModel <init>(...);
}

# === Room ===
-keep class * extends androidx.room.RoomDatabase
-keep class * extends androidx.room.Dao
-keep @androidx.room.Dao class *
-keep @androidx.room.Entity class *
-keepclassmembers class * {
    @androidx.room.* <methods>;
    @androidx.room.* <fields>;
}
-dontwarn androidx.room.paging.**

# === Coroutines ===
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}
-dontwarn kotlinx.coroutines.**

# === Kotlin ===
-keep class kotlin.Metadata { *; }
-keep class kotlin.** { *; }
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings {
    <fields>;
}

# === AndroidX ===
-keep class androidx.appcompat.** { *; }
-keep class androidx.lifecycle.** { *; }
-keep class androidx.navigation.** { *; }
-keep class androidx.paging.** { *; }
-keep class androidx.datastore.** { *; }

# === Glide ===
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder { *** rewind(); }
-dontwarn com.bumptech.glide.**

# === Markwon ===
-keep import.io.noties.markwon.** { *; }
-dontwarn io.noties.markwon.**

# === ExoPlayer / Media3 ===
-keep class androidx.media3.** { *; }
-dontwarn androidx.media3.**

# === MMKV ===
-keep class com.tencent.mmkv.** { *; }
-dontwarn com.tencent.mmkv.**

# === Custom Views ===
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

# === Activities / Fragments ===
-keep public class * extends android.app.Activity
-keep public class * extends androidx.appcompat.app.AppCompatActivity
-keep public class * extends androidx.fragment.app.Fragment
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgent

# === Parcelable ===
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}
-keepnames class * implements android.os.Parcelable

# === Serializable ===
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keepnames class * implements java.io.Serializable

# === Enums ===
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# === JNI ===
-keepclasseswithmembernames class * {
    native <methods>;
}

# === App Modules ===
-keep class com.aiai.app.** { *; }
-keep class com.aiai.chat.** { *; }
-keep class com.aiai.settings.** { *; }

# === DataStore ===
-keep class androidx.datastore.** { *; }
-dontwarn androidx.datastore.**

# === Paging ===
-keep class androidx.paging.** { *; }
-dontwarn androidx.paging.**

# === PendingIntent (Android 12+) ===
-keep,allowobfuscation,allowshrinking class android.app.PendingIntent

# === Keep BuildConfig ===
-keep class com.aiai.app.BuildConfig { *; }

# === Remove logging in release ===
-assumenosideeffects class android.util.Log {
    public static int d(...);
    public static int v(...);
    public static int i(...);
}

# === R8 optimization ===
-allowaccessmodification
-overloadaggressively
