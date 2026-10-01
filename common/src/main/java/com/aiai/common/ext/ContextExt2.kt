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
package com.aiai.common.ext

import android.content.Context
import android.widget.Toast

/**
 * Context 扩展函数集（扩展版）。
 */

/** 显示短 Toast。 */
fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

/** 显示长 Toast。 */
fun Context.toastLong(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}

/** dp 转 px。 */
fun Context.dp2px(dp: Float): Float {
    val density = resources.displayMetrics.density
    return dp * density
}

/** px 转 dp。 */
fun Context.px2dp(px: Float): Float {
    val density = resources.displayMetrics.density
    return px / density
}

/** sp 转 px。 */
fun Context.sp2px(sp: Float): Float {
    val scale = resources.displayMetrics.scaledDensity
    return sp * scale
}

/** 获取屏幕宽度。 */
fun Context.screenWidth(): Int {
    return resources.displayMetrics.widthPixels
}

/** 获取屏幕高度。 */
fun Context.screenHeight(): Int {
    return resources.displayMetrics.heightPixels
}

/** 获取状态栏高度。 */
fun Context.statusBarHeight(): Int {
    var result = 0
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = resources.getDimensionPixelSize(resourceId)
    }
    return result
}

/** 检查是否有网络。 */
fun Context.hasNetwork(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as? android.net.ConnectivityManager
    val info = cm?.activeNetworkInfo
    return info != null && info.isConnected
}

/** 获取版本名。 */
fun Context.versionName(): String {
    return try {
        packageManager.getPackageInfo(packageName, 0).versionName ?: "unknown"
    } catch (e: Exception) {
        "unknown"
    }
}

/** 获取版本号。 */
fun Context.versionCode(): Long {
    return try {
        val info = packageManager.getPackageInfo(packageName, 0)
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

/** 判断是否为 debug 版本。 */
fun Context.isDebug(): Boolean {
    return try {
        (applicationInfo.flags and android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE) != 0
    } catch (e: Exception) {
        false
    }
}

/** 获取 App 名称。 */
fun Context.appName(): String {
    return try {
        packageManager.getApplicationLabel(applicationInfo).toString()
    } catch (e: Exception) {
        ""
    }
}
