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
package com.aiai.chat.manager

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 更新管理器
 *
 * 检查应用更新，管理版本信息和更新提示。
 */
class UpdateManager(private val context: Context) {

    data class UpdateInfo(
        val versionCode: Int,
        val versionName: String,
        val downloadUrl: String,
        val releaseNotes: String,
        val isForceUpdate: Boolean = false,
        val minSupportedVersion: Int = 1
    )

    private val _updateInfo = MutableStateFlow<UpdateInfo?>(null)
    val updateInfo: StateFlow<UpdateInfo?> = _updateInfo.asStateFlow()

    private val _checking = MutableStateFlow(false)
    val checking: StateFlow<Boolean> = _checking.asStateFlow()

    suspend fun checkUpdate(currentVersionCode: Int): UpdateInfo? {
        _checking.value = true
        try {
            // 模拟检查更新
            val latestVersion = 100 // 假设最新版本号
            if (latestVersion > currentVersionCode) {
                _updateInfo.value = UpdateInfo(
                    versionCode = latestVersion,
                    versionName = "2.0.0",
                    downloadUrl = "https://example.com/app.apk",
                    releaseNotes = "1. 新增AI画图功能\n2. 优化流式输出速度\n3. 修复已知问题"
                )
            }
            return _updateInfo.value
        } finally {
            _checking.value = false
        }
    }

    fun dismissUpdate() {
        _updateInfo.value = null
    }
}
