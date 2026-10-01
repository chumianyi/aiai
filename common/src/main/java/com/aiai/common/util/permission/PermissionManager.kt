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
package com.aiai.common.util.permission

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * 权限管理器。
 *
 * 简化运行时权限请求流程。
 */
object PermissionManager {

    /** 检查是否已授予权限。 */
    fun hasPermission(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    /** 检查多个权限是否全部授予。 */
    fun hasPermissions(context: Context, vararg permissions: String): Boolean {
        return permissions.all { hasPermission(context, it) }
    }

    /** 请求权限。 */
    fun requestPermissions(activity: Activity, requestCode: Int, vararg permissions: String) {
        val needed = permissions.filterNot { hasPermission(activity, it) }.toTypedArray()
        if (needed.isNotEmpty()) {
            ActivityCompat.requestPermissions(activity, needed, requestCode)
        }
    }

    /** 判断是否应该显示权限解释。 */
    fun shouldShowRationale(activity: Activity, permission: String): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
    }
}
