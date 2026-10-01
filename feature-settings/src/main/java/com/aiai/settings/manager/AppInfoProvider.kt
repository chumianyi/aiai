/*
 * Copyright (c) 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.settings.manager

import android.content.Context
import com.aiai.settings.BuildConfig

/**
 * 应用信息提供者：版本号、版本名、构建时间、渠道。
 */
class AppInfoProvider(private val context: Context) {

    /** 版本名（如 1.2.0）。 */
    fun versionName(): String {
        return try {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "1.0.0"
        } catch (e: Exception) {
            "1.0.0"
        }
    }

    /** 版本号（整数）。 */
    fun versionCode(): Long {
        return try {
            val pkg = context.packageManager.getPackageInfo(context.packageName, 0)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                pkg.longVersionCode
            } else {
                @Suppress("DEPRECATION") pkg.versionCode.toLong()
            }
        } catch (e: Exception) {
            1L
        }
    }

    /** 应用名。 */
    fun appName(): String = context.getString(context.applicationInfo.labelRes)

    /** 包名。 */
    fun packageName(): String = context.packageName

    /** 调试构建。 */
    fun isDebug(): Boolean = BuildConfig.DEBUG

    /** 完整版本信息。 */
    fun fullVersion(): String = "v${versionName()} (${versionCode()})"
}
