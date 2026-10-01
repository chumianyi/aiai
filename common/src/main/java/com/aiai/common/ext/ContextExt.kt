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
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.common.ext

import android.app.Activity
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.util.TypedValue
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringDef
import androidx.core.content.ContextCompat

/**
 * Context 相关扩展函数集合。
 *
 * 提供 dp/sp/px 转换、资源获取、服务与广播操作、版本信息、权限检查等。
 */

// region 尺寸转换

/** dp 转 px。 */
val Float.dp: Float
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this,
        android.content.res.Resources.getSystem().displayMetrics)

/** Int dp 转 px。 */
val Int.dp: Float get() = toFloat().dp

/** sp 转 px。 */
val Float.sp: Float
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this,
        android.content.res.Resources.getSystem().displayMetrics)

/** Int sp 转 px。 */
val Int.sp: Float get() = toFloat().sp

/** px 转 dp。 */
val Float.pxToDp: Float
    get() = this / android.content.res.Resources.getSystem().displayMetrics.density

/** px 转 sp。 */
val Float.pxToSp: Float
    get() = this / android.content.res.Resources.getSystem().displayMetrics.scaledDensity

/** dp 转 Int px。 */
fun dp2px(dp: Float): Int = dp.dp.toInt()

// endregion

// region 资源获取

/** 从 [Context] 获取颜色。 */
fun Context.color(@ColorRes resId: Int): Int = ContextCompat.getColor(this, resId)

/** 从 [Context] 获取 Drawable。 */
fun Context.drawable(@DrawableRes resId: Int): Drawable? =
    ContextCompat.getDrawable(this, resId)

/** 从 [Context] 获取尺寸（像素）。 */
fun Context.dimenPx(@DimenRes resId: Int): Int = resources.getDimensionPixelSize(resId)

/** 从 [Context] 获取字符串。 */
fun Context.string(resId: Int): String = getString(resId)

/** 从 [Context] 获取格式化字符串。 */
fun Context.string(resId: Int, vararg args: Any): String = getString(resId, *args)

// endregion

// region 服务 & 广播

/** 启动 Service（安全处理异常）。 */
fun Context.startServiceSafe(intent: Intent): Boolean {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
        true
    } catch (e: Exception) {
        false
    }
}

/** 停止 Service。 */
fun Context.stopServiceSafe(intent: Intent): Boolean {
    return try {
        stopService(intent)
        true
    } catch (e: Exception) {
        false
    }
}

/** 动态注册广播。 */
fun Context.registerReceiverSafe(receiver: BroadcastReceiver, filter: IntentFilter) {
    try {
        ContextCompat.registerReceiver(this, receiver, filter, ContextCompat.RECEIVER_NOT_EXPORTED)
    } catch (e: Exception) {
        // 忽略重复注册
    }
}

/** 注销广播。 */
fun Context.unregisterReceiverSafe(receiver: BroadcastReceiver) {
    try {
        unregisterReceiver(receiver)
    } catch (e: IllegalArgumentException) {
        // 未注册过，忽略
    }
}

/** 发送广播。 */
fun Context.sendBroadcastSafe(intent: Intent) {
    try {
        sendBroadcast(intent)
    } catch (e: Exception) {
        // 忽略
    }
}

// endregion

// region 版本 & 权限

/** 获取应用版本名。 */
fun Context.versionName(): String {
    return try {
        val pm = packageManager
        val info = pm.getPackageInfo(packageName, 0)
        info.versionName ?: "unknown"
    } catch (e: PackageManager.NameNotFoundException) {
        "unknown"
    }
}

/** 获取应用版本号。 */
fun Context.versionCode(): Long {
    return try {
        val pm = packageManager
        val info = pm.getPackageInfo(packageName, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) info.longVersionCode
        else @Suppress("DEPRECATION") info.versionCode.toLong()
    } catch (e: PackageManager.NameNotFoundException) {
        0L
    }
}

/** 判断是否已授予权限。 */
fun Context.hasPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

/** 判断是否有悬浮窗权限。 */
fun Context.canDrawOverlays(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        android.provider.Settings.canDrawOverlays(this)
    } else true
}

/** 判断是否有安装未知来源权限。 */
fun Context.canInstallUnknown(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        packageManager.canRequestPackageInstalls()
    } else true
}

/** 判断是否为调试版。 */
fun Context.isDebug(): Boolean {
    return try {
        val info = packageManager.getApplicationInfo(packageName, 0)
        (info.flags and android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE) != 0
    } catch (e: Exception) {
        false
    }
}

// endregion

// region 其他

/** 获取 DisplayMetrics 密度。 */
val Context.screenDensity: Float
    get() = resources.displayMetrics.density

/** 获取屏幕宽度（像素）。 */
val Context.screenWidth: Int
    get() = resources.displayMetrics.widthPixels

/** 获取屏幕高度（像素）。 */
val Context.screenHeight: Int
    get() = resources.displayMetrics.heightPixels

/** 跳转到系统设置页。 */
fun Context.openAppSettings() {
    val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", packageName, null)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(intent)
}

/** 分享文本内容。 */
fun Context.shareText(text: String, chooserTitle: String = "分享到") {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    startActivity(Intent.createChooser(intent, chooserTitle).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    })
}

/** 获取 Manifest 中 meta-data 字符串值。 */
fun Context.metaData(name: String): String? {
    return try {
        val ai = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
        ai.metaData?.getString(name)
    } catch (e: Exception) {
        null
    }
}

/** 启动浏览器打开 URL。 */
fun Context.openBrowser(url: String): Boolean {
    return try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
        true
    } catch (e: Exception) {
        false
    }
}

/** 安装 APK 文件。 */
fun Context.installApk(uri: Uri) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "application/vnd.android.package-archive")
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    startActivity(intent)
}

/** 卸载应用。 */
fun Context.uninstallPackage(packageName: String) {
    val intent = Intent(Intent.ACTION_DELETE, Uri.parse("package:$packageName"))
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}

/** 判断当前是否处于前台（通过 ActivityManager）。 */
fun Context.isForeground(): Boolean {
    val am = getSystemService(Context.ACTIVITY_SERVICE) as? android.app.ActivityManager
    val processes = am?.runningAppProcesses ?: return false
    return processes.any {
        it.processName == packageName &&
            it.importance == android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
    }
}

// endregion
