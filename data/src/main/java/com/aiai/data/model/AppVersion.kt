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
package com.aiai.data.model

/**
 * 版本域模型。
 *
 * 表示应用版本信息。
 */
data class AppVersion(
    val version: String,
    val versionCode: Int,
    val changelog: String,
    val downloadUrl: String,
    val fileSize: Long,
    val forceUpdate: Boolean,
    val minSupportedVersion: String,
    val publishedAt: Long,
    val isBeta: Boolean = false
) {
    /**
     * 检查是否需要更新。
     *
     * @param currentVersionCode 当前版本代码
     * @return true 表示需要更新
     */
    fun needsUpdate(currentVersionCode: Int): Boolean {
        return versionCode > currentVersionCode
    }

    /**
     * 获取格式化的文件大小。
     */
    fun getFormattedFileSize(): String {
        return when {
            fileSize < 1024 -> "${fileSize}B"
            fileSize < 1024 * 1024 -> "${"%.1f".format(fileSize / 1024.0)}KB"
            else -> "${"%.1f".format(fileSize / (1024.0 * 1024.0))}MB"
        }
    }
}
