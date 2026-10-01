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
 * 应用版本信息管理器
 *
 * 管理应用版本信息和更新日志。
 */
class AppInfoManager(private val context: Context) {

    data class VersionInfo(
        val versionCode: Long,
        val versionName: String,
        val buildType: String,
        val releaseNotes: String
    )

    private val _versionInfo = MutableStateFlow<VersionInfo?>(null)
    val versionInfo: StateFlow<VersionInfo?> = _versionInfo.asStateFlow()

    init {
        loadVersionInfo()
    }

    private fun loadVersionInfo() {
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            _versionInfo.value = VersionInfo(
                versionCode = packageInfo.longVersionCode,
                versionName = packageInfo.versionName ?: "1.0.0",
                buildType = "release",
                releaseNotes = "1. 全新AI对话体验\n2. 支持流式输出\n3. 多模型切换"
            )
        } catch (e: Exception) {
            _versionInfo.value = VersionInfo(1, "1.0.0", "debug", "")
        }
    }

    fun getAppName(): String {
        return context.getString(context.applicationInfo.labelRes)
    }

    fun getVersionName(): String {
        return _versionInfo.value?.versionName ?: "unknown"
    }
}
