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
 * See the License for the specific permissions and
 * limitations under the License.
 */
package com.aiai.common.util.other

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

/**
 * 设备信息工具类。
 */
object DeviceInfoUtil {

    /** 设备品牌。 */
    fun getBrand(): String = Build.BRAND

    /** 设备型号。 */
    fun getModel(): String = Build.MODEL

    /** 设备厂商。 */
    fun getManufacturer(): String = Build.MANUFACTURER

    /** SDK 版本。 */
    fun getSdkInt(): Int = Build.VERSION.SDK_INT

    /** 系统版本。 */
    fun getRelease(): String = Build.VERSION.RELEASE ?: "unknown"

    /** 获取 Android ID。 */
    fun getAndroidId(context: Context): String {
        return android.provider.Settings.Secure.getString(
            context.contentResolver,
            android.provider.Settings.Secure.ANDROID_ID
        ) ?: "unknown"
    }

    /** 是否为模拟器。 */
    fun isEmulator(): Boolean {
        return (Build.FINGERPRINT.startsWith("generic")
            || Build.FINGERPRINT.startsWith("unknown")
            || Build.MODEL.contains("google_sdk")
            || Build.MODEL.contains("Emulator")
            || Build.MANUFACTURER.contains("Genymotion"))
    }

    /** 是否有相机。 */
    fun hasCamera(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
    }
}
