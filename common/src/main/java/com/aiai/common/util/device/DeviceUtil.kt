/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language permissions and
 * limitations under the License.
 */
package com.aiai.common.util.device

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build

/**
 * 设备信息工具类。
 *
 * 提供设备型号、厂商、系统版本、屏幕参数等信息。
 */
object DeviceUtil {

    /** 设备厂商。 */
    fun manufacturer(): String = Build.MANUFACTURER

    /** 设备型号。 */
    fun model(): String = Build.MODEL

    /** 设备品牌。 */
    fun brand(): String = Build.BRAND

    /** Android 系统版本号（如 14）。 */
    fun sdkInt(): Int = Build.VERSION.SDK_INT

    /** Android 版本名（如 "14"）。 */
    fun release(): String = Build.VERSION.RELEASE ?: "unknown"

    /** 设备产品名。 */
    fun product(): String = Build.PRODUCT

    /** 获取屏幕宽度（像素）。 */
    fun screenWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    /** 获取屏幕高度（像素）。 */
    fun screenHeight(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }

    /** 获取屏幕密度（dpi）。 */
    fun screenDensityDpi(context: Context): Int {
        return context.resources.displayMetrics.densityDpi
    }

    /** 获取屏幕密度倍率。 */
    fun screenDensity(context: Context): Float {
        return context.resources.displayMetrics.density
    }

    /** 判断是否为平板。 */
    fun isTablet(context: Context): Boolean {
        return (context.resources.configuration.screenLayout and android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK) >=
            android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    /** 判断是否为模拟器。 */
    fun isEmulator(): Boolean {
        return (Build.FINGERPRINT.startsWith("generic")
            || Build.FINGERPRINT.startsWith("unknown")
            || Build.MODEL.contains("google_sdk")
            || Build.MODEL.contains("Emulator")
            || Build.MODEL.contains("Android SDK built for x86")
            || Build.MANUFACTURER.contains("Genymotion")
            || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
            || "google_sdk" == Build.PRODUCT)
    }

    /** 获取唯一设备 ID（Android ID）。 */
    @SuppressLint("HardwareIds")
    fun getAndroidId(context: Context): String {
        return android.provider.Settings.Secure.getString(
            context.contentResolver,
            android.provider.Settings.Secure.ANDROID_ID
        ) ?: "unknown"
    }
}
