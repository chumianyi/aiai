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
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable

/**
 * 应用信息工具类。
 */
object AppInfoUtil {

    /** 获取应用版本名。 */
    fun getVersionName(context: Context): String {
        return try {
            val pm = context.packageManager
            val info = pm.getPackageInfo(context.packageName, 0)
            info.versionName ?: "unknown"
        } catch (e: Exception) {
            "unknown"
        }
    }

    /** 获取应用版本号。 */
    fun getVersionCode(context: Context): Long {
        return try {
            val pm = context.packageManager
            val info = pm.getPackageInfo(context.packageName, 0)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                info.longVersionCode
            } else {
                @Suppress("DEPRECATION")
                info.versionCode.toLong()
            }
        } catch (e: Exception) {
            0L
        }
    }

    /** 获取应用名称。 */
    fun getAppName(context: Context): String {
        return try {
            val pm = context.packageManager
            val info = pm.getApplicationInfo(context.packageName, 0)
            pm.getApplicationLabel(info).toString()
        } catch (e: Exception) {
            ""
        }
    }

    /** 获取应用图标。 */
    fun getAppIcon(context: Context): Drawable? {
        return try {
            val pm = context.packageManager
            pm.getApplicationIcon(context.packageName)
        } catch (e: Exception) {
            null
        }
    }

    /** 判断是否为系统应用。 */
    fun isSystemApp(context: Context): Boolean {
        return try {
            val pm = context.packageManager
            val info = pm.getApplicationInfo(context.packageName, 0)
            (info.flags and ApplicationInfo.FLAG_SYSTEM) != 0
        } catch (e: Exception) {
            false
        }
    }

    /** 获取已安装应用列表。 */
    fun getInstalledApps(context: Context): List<ApplicationInfo> {
        val pm = context.packageManager
        return pm.getInstalledApplications(PackageManager.GET_META_DATA)
    }
}
