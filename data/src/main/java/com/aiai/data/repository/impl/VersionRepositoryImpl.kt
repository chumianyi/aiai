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
package com.aiai.data.repository.impl

import android.util.Log
import com.aiai.data.model.AppVersion
import com.aiai.data.repository.VersionRepository

/**
 * 版本仓库实现类。
 *
 * 提供版本检查、更新日志、下载等功能。
 */
class VersionRepositoryImpl : VersionRepository {

    companion object {
        private const val TAG = "VersionRepositoryImpl"
        private const val CURRENT_VERSION_CODE = 1
        private const val CURRENT_VERSION_NAME = "1.0.0"
    }

    override suspend fun checkUpdate(): Result<AppVersion> {
        Log.d(TAG, "Check for updates")
        return try {
            // 模拟检查更新
            val hasUpdate = false // 模拟当前已是最新版本
            if (hasUpdate) {
                val newVersion = AppVersion(
                    versionCode = 2,
                    versionName = "1.1.0",
                    updateLog = "1. 新增功能\n2. 修复bug",
                    downloadUrl = "https://example.com/app.apk",
                    forceUpdate = false,
                    releaseDate = System.currentTimeMillis()
                )
                Result.success(newVersion)
            } else {
                Result.success(AppVersion(
                    versionCode = CURRENT_VERSION_CODE,
                    versionName = CURRENT_VERSION_NAME,
                    updateLog = "",
                    downloadUrl = "",
                    forceUpdate = false,
                    releaseDate = System.currentTimeMillis()
                ))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Check update failed", e)
            Result.failure(e)
        }
    }

    override suspend fun getUpdateLog(): Result<String> {
        Log.d(TAG, "Get update log")
        return try {
            val log = buildString {
                appendLine("v1.0.0")
                appendLine("- 首次发布")
                appendLine("- 基础聊天功能")
                appendLine("- 多模型支持")
            }
            Result.success(log)
        } catch (e: Exception) {
            Log.e(TAG, "Get update log failed", e)
            Result.failure(e)
        }
    }

    override fun getCurrentVersionName(): String = CURRENT_VERSION_NAME

    override fun getCurrentVersionCode(): Int = CURRENT_VERSION_CODE
}
