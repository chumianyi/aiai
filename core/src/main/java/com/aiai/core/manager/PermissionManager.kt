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
package com.aiai.core.manager

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

/**
 * 权限管理器。
 *
 * 提供运行时权限、特殊权限、权限组等功能。
 */
class PermissionManager(private val context: Context) {

    /**
     * 检查单个权限是否已授予。
     *
     * @param permission 权限名称
     * @return true 表示已授予
     */
    fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) ==
                PackageManager.PERMISSION_GRANTED
    }

    /**
     * 检查多个权限是否全部已授予。
     *
     * @param permissions 权限列表
     * @return true 表示全部已授予
     */
    fun hasAllPermissions(vararg permissions: String): Boolean {
        return permissions.all { hasPermission(it) }
    }

    /**
     * 获取未授予的权限列表。
     *
     * @param permissions 权限列表
     * @return 未授予的权限列表
     */
    fun getDeniedPermissions(vararg permissions: String): List<String> {
        return permissions.filter { !hasPermission(it) }
    }

    /**
     * 检查是否为危险权限。
     *
     * @param permission 权限名称
     * @return true 表示是危险权限
     */
    fun isDangerousPermission(permission: String): Boolean {
        return when (permission) {
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.WRITE_CONTACTS,
            android.Manifest.permission.READ_CALL_LOG,
            android.Manifest.permission.WRITE_CALL_LOG,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_CALENDAR,
            android.Manifest.permission.WRITE_CALENDAR,
            android.Manifest.permission.READ_SMS,
            android.Manifest.permission.SEND_SMS,
            android.Manifest.permission.READ_PHONE_STATE -> true
            else -> false
        }
    }

    /**
     * 检查是否为特殊权限。
     *
     * @param permission 权限名称
     * @return true 表示是特殊权限
     */
    fun isSpecialPermission(permission: String): Boolean {
        return when (permission) {
            android.Manifest.permission.SYSTEM_ALERT_WINDOW,
            android.Manifest.permission.WRITE_SETTINGS,
            android.Manifest.permission.PACKAGE_USAGE_STATS,
            android.Manifest.permission.REQUEST_INSTALL_PACKAGES -> true
            else -> false
        }
    }

    /**
     * 获取权限组。
     *
     * @param permission 权限名称
     * @return 权限组名称
     */
    fun getPermissionGroup(permission: String): String {
        return when (permission) {
            android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.WRITE_CONTACTS,
            android.Manifest.permission.GET_ACCOUNTS -> "android.permission-group.CONTACTS"
            android.Manifest.permission.READ_CALL_LOG,
            android.Manifest.permission.WRITE_CALL_LOG,
            android.Manifest.permission.CALL_PHONE,
            android.Manifest.permission.READ_PHONE_STATE -> "android.permission-group.PHONE"
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION -> "android.permission-group.LOCATION"
            android.Manifest.permission.CAMERA -> "android.permission-group.CAMERA"
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE -> "android.permission-group.STORAGE"
            android.Manifest.permission.RECORD_AUDIO -> "android.permission-group.MICROPHONE"
            android.Manifest.permission.READ_CALENDAR,
            android.Manifest.permission.WRITE_CALENDAR -> "android.permission-group.CALENDAR"
            android.Manifest.permission.READ_SMS,
            android.Manifest.permission.SEND_SMS,
            android.Manifest.permission.RECEIVE_SMS -> "android.permission-group.SMS"
            else -> "未知权限组"
        }
    }

    /**
     * 获取权限说明文本。
     *
     * @param permission 权限名称
     * @return 权限说明
     */
    fun getPermissionRationale(permission: String): String {
        return when (permission) {
            android.Manifest.permission.CAMERA -> "需要相机权限来拍摄照片和扫描二维码"
            android.Manifest.permission.RECORD_AUDIO -> "需要录音权限来录制语音消息"
            android.Manifest.permission.ACCESS_FINE_LOCATION -> "需要定位权限来提供附近服务"
            android.Manifest.permission.READ_CONTACTS -> "需要读取联系人权限来选择联系人"
            android.Manifest.permission.READ_EXTERNAL_STORAGE -> "需要存储权限来保存和读取文件"
            android.Manifest.permission.READ_CALENDAR -> "需要日历权限来添加日程提醒"
            else -> "需要此权限来提供完整功能"
        }
    }

    /**
     * 检查悬浮窗权限。
     *
     * @return true 表示已授权
     */
    fun hasOverlayPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            android.provider.Settings.canDrawOverlays(context)
        } else {
            true
        }
    }

    /**
     * 检查是否可以安装未知来源应用。
     *
     * @return true 表示已授权
     */
    fun hasInstallUnknownAppPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.packageManager.canRequestPackageInstalls()
        } else {
            true
        }
    }
}
