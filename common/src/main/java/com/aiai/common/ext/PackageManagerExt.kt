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

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PermissionInfo
import android.graphics.drawable.Drawable
import android.os.Build

/**
 * PackageManager 扩展函数集合。
 *
 * 提供应用列表、版本信息、签名、权限等常用包管理操作。
 */

// region 版本信息

/**
 * 获取应用版本名。
 *
 * @param packageName 包名，默认当前应用
 * @return 版本名字符串
 */
fun Context.getVersionName(packageName: String = this.packageName): String {
    return try {
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        packageInfo.versionName ?: ""
    } catch (e: PackageManager.NameNotFoundException) {
        ""
    }
}

/**
 * 获取应用版本号。
 *
 * @param packageName 包名，默认当前应用
 * @return 版本号
 */
fun Context.getVersionCode(packageName: String = this.packageName): Long {
    return try {
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode
        } else {
            @Suppress("DEPRECATION")
            packageInfo.versionCode.toLong()
        }
    } catch (e: PackageManager.NameNotFoundException) {
        0L
    }
}

/**
 * 获取应用版本信息。
 *
 * @param packageName 包名
 * @return PackageInfo 对象
 */
fun Context.getPackageInfo(packageName: String = this.packageName): PackageInfo? {
    return try {
        packageManager.getPackageInfo(packageName, 0)
    } catch (e: PackageManager.NameNotFoundException) {
        null
    }
}

/**
 * 获取应用名称。
 *
 * @param packageName 包名
 * @return 应用名称
 */
fun Context.getAppName(packageName: String = this.packageName): String {
    return try {
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        packageManager.getApplicationLabel(packageInfo.applicationInfo).toString()
    } catch (e: PackageManager.NameNotFoundException) {
        ""
    }
}

/**
 * 获取应用图标。
 *
 * @param packageName 包名
 * @return 应用图标 Drawable
 */
fun Context.getAppIcon(packageName: String = this.packageName): Drawable? {
    return try {
        packageManager.getApplicationIcon(packageName)
    } catch (e: PackageManager.NameNotFoundException) {
        null
    }
}

// endregion

// region 应用列表

/**
 * 获取所有已安装应用列表。
 *
 * @param includeSystem 是否包含系统应用
 * @return 已安装应用信息列表
 */
fun Context.getInstalledApps(includeSystem: Boolean = false): List<ApplicationInfo> {
    val flags = if (includeSystem) 0 else PackageManager.GET_META_DATA
    return packageManager.getInstalledApplications(flags).filter { app ->
        if (includeSystem) {
            true
        } else {
            app.flags and ApplicationInfo.FLAG_SYSTEM == 0
        }
    }
}

/**
 * 获取第三方应用列表。
 *
 * @return 第三方应用信息列表
 */
fun Context.getThirdPartyApps(): List<ApplicationInfo> {
    return packageManager.getInstalledApplications(0).filter { app ->
        app.flags and ApplicationInfo.FLAG_SYSTEM == 0
    }
}

/**
 * 检查应用是否已安装。
 *
 * @param packageName 要检查的包名
 * @return true 表示已安装
 */
fun Context.isAppInstalled(packageName: String): Boolean {
    return try {
        packageManager.getPackageInfo(packageName, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

/**
 * 获取应用安装时间。
 *
 * @param packageName 包名
 * @return 安装时间戳
 */
fun Context.getAppInstallTime(packageName: String = this.packageName): Long {
    return try {
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        packageInfo.firstInstallTime
    } catch (e: PackageManager.NameNotFoundException) {
        0L
    }
}

/**
 * 获取应用最后更新时间。
 *
 * @param packageName 包名
 * @return 更新时间戳
 */
fun Context.getAppUpdateTime(packageName: String = this.packageName): Long {
    return try {
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        packageInfo.lastUpdateTime
    } catch (e: PackageManager.NameNotFoundException) {
        0L
    }
}

// endregion

// region 权限

/**
 * 检查是否已授予权限。
 *
 * @param permission 权限名称
 * @return true 表示已授予
 */
/**
 * 检查多个权限是否全部授予。
 *
 * @param permissions 权限列表
 * @return true 表示全部已授予
 */
fun Context.hasPermissions(vararg permissions: String): Boolean {
    return permissions.all { hasPermission(it) }
}

/**
 * 获取未授予的权限列表。
 *
 * @param permissions 要检查的权限列表
 * @return 未授予的权限列表
 */
fun Context.getDeniedPermissions(vararg permissions: String): List<String> {
    return permissions.filter { !hasPermission(it) }
}

/**
 * 获取应用声明的所有权限。
 *
 * @param packageName 包名
 * @return 权限列表
 */
fun Context.getDeclaredPermissions(packageName: String = this.packageName): List<String> {
    return try {
        val packageInfo = packageManager.getPackageInfo(
            packageName,
            PackageManager.GET_PERMISSIONS
        )
        packageInfo.requestedPermissions?.toList() ?: emptyList()
    } catch (e: PackageManager.NameNotFoundException) {
        emptyList()
    }
}

/**
 * 获取权限信息。
 *
 * @param permissionName 权限名称
 * @return PermissionInfo 对象
 */
fun Context.getPermissionInfo(permissionName: String): PermissionInfo? {
    return try {
        packageManager.getPermissionInfo(permissionName, 0)
    } catch (e: PackageManager.NameNotFoundException) {
        null
    }
}

/**
 * 检查是否为特殊权限。
 *
 * @param permission 权限名称
 * @return true 表示是特殊权限
 */
fun Context.isSpecialPermission(permission: String): Boolean {
    return when (permission) {
        android.Manifest.permission.SYSTEM_ALERT_WINDOW,
        android.Manifest.permission.WRITE_SETTINGS,
        android.Manifest.permission.PACKAGE_USAGE_STATS,
        android.Manifest.permission.REQUEST_INSTALL_PACKAGES -> true
        else -> false
    }
}

// endregion

// region 签名

/**
 * 获取应用签名信息。
 *
 * @param packageName 包名
 * @return 签名字节数组
 */
fun Context.getAppSignature(packageName: String = this.packageName): ByteArray? {
    return try {
        val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
        } else {
            @Suppress("DEPRECATION")
            packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.signingInfo?.apkContentsSigners?.firstOrNull()?.toByteArray()
        } else {
            @Suppress("DEPRECATION")
            packageInfo.signatures?.firstOrNull()?.toByteArray()
        }
    } catch (e: PackageManager.NameNotFoundException) {
        null
    }
}

/**
 * 获取应用签名的 MD5 值。
 *
 * @param packageName 包名
 * @return MD5 字符串
 */
fun Context.getAppSignatureMd5(packageName: String = this.packageName): String {
    val signature = getAppSignature(packageName) ?: return ""
    val md = java.security.MessageDigest.getInstance("MD5")
    val digest = md.digest(signature)
    return digest.joinToString(":") { "%02X".format(it) }
}

/**
 * 获取应用签名的 SHA1 值。
 *
 * @param packageName 包名
 * @return SHA1 字符串
 */
fun Context.getAppSignatureSha1(packageName: String = this.packageName): String {
    val signature = getAppSignature(packageName) ?: return ""
    val md = java.security.MessageDigest.getInstance("SHA1")
    val digest = md.digest(signature)
    return digest.joinToString(":") { "%02X".format(it) }
}

// endregion

// region 其他

/**
 * 获取应用数据目录。
 *
 * @param packageName 包名
 * @return 数据目录路径
 */
fun Context.getAppDataDir(packageName: String = this.packageName): String {
    return try {
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        packageInfo.applicationInfo.dataDir
    } catch (e: PackageManager.NameNotFoundException) {
        ""
    }
}

/**
 * 获取应用安装来源。
 *
 * @param packageName 包名
 * @return 安装来源包名
 */
fun Context.getInstallerSource(packageName: String = this.packageName): String {
    return packageManager.getInstallerPackageName(packageName) ?: ""
}

/**
 * 检查是否为系统应用。
 *
 * @param packageName 包名
 * @return true 表示是系统应用
 */
fun Context.isSystemApp(packageName: String = this.packageName): Boolean {
    return try {
        val appInfo = packageManager.getApplicationInfo(packageName, 0)
        appInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

/**
 * 获取应用大小（近似值）。
 *
 * @param packageName 包名
 * @return 应用大小（字节）
 */
fun Context.getAppSize(packageName: String = this.packageName): Long {
    return try {
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        val sourceDir = packageInfo.applicationInfo.sourceDir
        val file = java.io.File(sourceDir)
        file.length()
    } catch (e: Exception) {
        0L
    }
}

/**
 * 启动应用。
 *
 * @param packageName 要启动的应用包名
 * @return true 表示启动成功
 */
fun Context.launchApp(packageName: String): Boolean {
    val intent = packageManager.getLaunchIntentForPackage(packageName) ?: return false
    intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
    return true
}

/**
 * 卸载应用。
 *
 * @param packageName 要卸载的应用包名
 */
fun Context.uninstallApp(packageName: String) {
    val intent = android.content.Intent(
        android.content.Intent.ACTION_DELETE,
        android.net.Uri.parse("package:$packageName")
    )
    intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}

/**
 * 打开应用详情设置页。
 *
 * @param packageName 包名
 */
fun Context.openAppDetailSettings(packageName: String = this.packageName) {
    val intent = android.content.Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.data = android.net.Uri.parse("package:$packageName")
    intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}

// endregion
