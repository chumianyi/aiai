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
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * Activity Result API 封装管理器。
 *
 * 提供统一的 Activity 跳转和结果回调管理。
 */
class ActivityResultManager {

    private val launchers = mutableMapOf<String, ActivityResultLauncher<*>>()

    /**
     * 注册启动器。
     *
     * @param activity FragmentActivity
     * @param key 唯一标识
     * @param contract ActivityResultContract
     * @param callback 结果回调
     */
    fun <I, O> register(
        activity: FragmentActivity,
        key: String,
        contract: ActivityResultContract<I, O>,
        callback: ActivityResultCallback<O>
    ) {
        val launcher = activity.activityResultRegistry.register(key, contract, callback)
        launchers[key] = launcher
    }

    /**
     * 注册启动器（Fragment 中使用）。
     *
     * @param fragment Fragment
     * @param key 唯一标识
     * @param contract ActivityResultContract
     * @param callback 结果回调
     */
    fun <I, O> register(
        fragment: Fragment,
        key: String,
        contract: ActivityResultContract<I, O>,
        callback: ActivityResultCallback<O>
    ) {
        val launcher = fragment.registerForActivityResult(contract, callback)
        launchers[key] = launcher
    }

    /**
     * 启动。
     *
     * @param key 唯一标识
     * @param input 输入参数
     */
    @Suppress("UNCHECKED_CAST")
    fun <I> launch(key: String, input: I) {
        val launcher = launchers[key] as? ActivityResultLauncher<I>
        launcher?.launch(input)
    }

    /**
     * 启动（无输入参数）。
     *
     * @param key 唯一标识
     */
    fun launch(key: String) {
        launch<Unit>(key, Unit)
    }

    /**
     * 注销启动器。
     *
     * @param key 唯一标识
     */
    fun unregister(key: String) {
        launchers.remove(key)?.unregister()
    }

    /**
     * 注销所有启动器。
     */
    fun unregisterAll() {
        launchers.values.forEach { it.unregister() }
        launchers.clear()
    }

    companion object {
        /** 请求权限的 key。 */
        const val KEY_REQUEST_PERMISSION = "request_permission"

        /** 多个权限请求的 key。 */
        const val KEY_REQUEST_MULTIPLE_PERMISSIONS = "request_multiple_permissions"

        /** 拍照的 key。 */
        const val KEY_TAKE_PHOTO = "take_photo"

        /** 选择图片的 key。 */
        const val KEY_PICK_IMAGE = "pick_image"

        /** 选择视频的 key。 */
        const val KEY_PICK_VIDEO = "pick_video"

        /** 选择文件的 key。 */
        const val KEY_PICK_FILE = "pick_file"
    }
}
