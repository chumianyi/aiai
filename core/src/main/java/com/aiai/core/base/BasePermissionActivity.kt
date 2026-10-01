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
package com.aiai.core.base

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.viewbinding.ViewBinding

/**
 * 权限请求基类。
 *
 * 功能：
 * - 批量请求权限
 * - Rationale 说明
 * - 永久拒绝处理
 * - 跳转到设置页
 */
abstract class BasePermissionActivity<VB : ViewBinding> : BaseActivity<VB>() {

    /** 权限请求回调。 */
    private var permissionCallback: ((Map<String, Boolean>) -> Unit)? = null

    /** 权限请求 launcher。 */
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        permissionCallback?.invoke(result)
        permissionCallback = null
    }

    /**
     * 请求权限。
     *
     * @param permissions 要请求的权限列表
     * @param callback 结果回调
     */
    protected open fun requestPermissions(
        vararg permissions: String,
        callback: (Map<String, Boolean>) -> Unit
    ) {
        permissionCallback = callback
        permissionLauncher.launch(permissions.toList().toTypedArray())
    }

    /**
     * 检查是否已授予所有权限。
     *
     * @param permissions 权限列表
     * @return true 表示全部已授予
     */
    protected open fun hasAllPermissions(vararg permissions: String): Boolean {
        return permissions.all {
            checkSelfPermission(it) == android.content.pm.PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * 获取未授予的权限列表。
     *
     * @param permissions 权限列表
     * @return 未授予的权限列表
     */
    protected open fun getDeniedPermissions(vararg permissions: String): List<String> {
        return permissions.filter {
            checkSelfPermission(it) != android.content.pm.PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * 检查是否需要显示 Rationale。
     *
     * @param permission 权限
     * @return true 表示需要显示说明
     */
    protected open fun shouldShowRationale(permission: String): Boolean {
        return shouldShowRequestPermissionRationale(permission)
    }

    /**
     * 跳转到应用设置页。
     */
    protected open fun openAppSettings() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        )
        startActivity(intent)
    }

    /**
     * 检查权限是否被永久拒绝（勾选了"不再询问"）。
     *
     * @param permission 权限
     * @return true 表示永久拒绝
     */
    protected open fun isPermissionPermanentlyDenied(permission: String): Boolean {
        return checkSelfPermission(permission) != android.content.pm.PackageManager.PERMISSION_GRANTED &&
                !shouldShowRequestPermissionRationale(permission)
    }
}
