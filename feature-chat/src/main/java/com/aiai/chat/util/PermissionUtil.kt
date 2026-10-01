/*
 * Copyright (c) 2026 爱Ai (AiAi)
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
package com.aiai.chat.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

/**
 * 权限工具类
 *
 * 提供运行时权限检查和请求的便捷方法。
 */
object PermissionUtil {

    /**
     * 需要的权限列表
     */
    val REQUIRED_PERMISSIONS = mutableListOf<String>().apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(android.Manifest.permission.POST_NOTIFICATIONS)
            add(android.Manifest.permission.READ_MEDIA_IMAGES)
            add(android.Manifest.permission.READ_MEDIA_AUDIO)
        } else {
            add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        add(android.Manifest.permission.RECORD_AUDIO)
        add(android.Manifest.permission.CAMERA)
    }.toTypedArray()

    /**
     * 检查单个权限是否已授权
     */
    fun hasPermission(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * 检查多个权限是否全部已授权
     */
    fun hasPermissions(context: Context, permissions: Array<String>): Boolean {
        return permissions.all { hasPermission(context, it) }
    }

    /**
     * 检查所有必需权限是否已授权
     */
    fun hasRequiredPermissions(context: Context): Boolean {
        return hasPermissions(context, REQUIRED_PERMISSIONS)
    }

    /**
     * 获取未授权的权限列表
     */
    fun getDeniedPermissions(context: Context, permissions: Array<String>): List<String> {
        return permissions.filterNot { hasPermission(context, it) }
    }

    /**
     * 是否应该显示权限解释
     */
    fun shouldShowRequestRationale(activity: android.app.Activity, permission: String): Boolean {
        return activity.shouldShowRequestPermissionRationale(permission)
    }

    /**
     * 相机权限
     */
    fun hasCameraPermission(context: Context): Boolean {
        return hasPermission(context, android.Manifest.permission.CAMERA)
    }

    /**
     * 录音权限
     */
    fun hasRecordAudioPermission(context: Context): Boolean {
        return hasPermission(context, android.Manifest.permission.RECORD_AUDIO)
    }

    /**
     * 通知权限
     */
    fun hasNotificationPermission(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true
        return hasPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
    }
}
