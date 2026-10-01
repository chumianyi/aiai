plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.aiai.common"
    compileSdk = 34
    defaultConfig { minSdk = 24; consumerProguardFiles("consumer-rules.pro") }
    buildTypes { release { isMinifyEnabled = false } }
    compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
    kotlinOptions { jvmTarget = "17" }
}

dependencies {
    api(libs.androidx.core.ktx)
    api(libs.androidx.appcompat)
    api(libs.kotlin.stdlib)
    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.android)
    api(libs.mmkv)
    api(libs.gson)
    api(libs.datastore.preferences)
    api("androidx.biometric:biometric:1.1.0")
    api("org.bouncycastle:bcprov-jdk15to18:1.78")
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.truth)
}
