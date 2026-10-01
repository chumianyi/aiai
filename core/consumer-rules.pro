# Consumer ProGuard rules for AiAi module
-keep class com.aiai.** { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
