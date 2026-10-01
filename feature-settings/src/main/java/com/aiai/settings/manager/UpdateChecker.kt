/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.settings.manager

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

/** 检查更新结果。 */
data class UpdateInfo(
    val hasUpdate: Boolean,
    val latestVersion: String,
    val versionCode: Int,
    val changelog: List<String>,
    val downloadUrl: String,
    val sizeBytes: Long,
    val isForce: Boolean = false
)

/**
 * 版本更新检查。
 *
 * 实现版本号比较语义化（major.minor.patch），模拟从服务器拉取更新信息，
 * 提供下载进度流。
 */
class UpdateChecker(private val context: Context) {

    private val _downloadProgress = MutableStateFlow(0)
    /** 下载进度 0~100。 */
    val downloadProgress: StateFlow<Int> = _downloadProgress.asStateFlow()

    /** 当前版本名。 */
    fun currentVersion(): String {
        return try {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "1.0.0"
        } catch (e: Exception) {
            "1.0.0"
        }
    }

    /**
     * 比较两个语义化版本 [a] 与 [b]：正数表示 a > b。
     */
    fun compareVersion(a: String, b: String): Int {
        val pa = a.trim().removePrefix("v").split(".")
        val pb = b.trim().removePrefix("v").split(".")
        val len = maxOf(pa.size, pb.size)
        for (i in 0 until len) {
            val na = pa.getOrNull(i)?.toIntOrNull() ?: 0
            val nb = pb.getOrNull(i)?.toIntOrNull() ?: 0
            if (na != nb) return na - nb
        }
        return 0
    }

    /**
     * 模拟从服务器检查更新。
     */
    suspend fun check(): UpdateInfo = withContext(Dispatchers.IO) {
        delay(600) // 模拟网络
        val latest = "1.2.0"
        val hasUpdate = compareVersion(latest, currentVersion()) > 0
        UpdateInfo(
            hasUpdate = hasUpdate,
            latestVersion = latest,
            versionCode = 12,
            changelog = listOf(
                "新增语音输入设置",
                "优化深色模式显示",
                "修复若干已知问题"
            ),
            downloadUrl = "https://example.com/aiai.apk",
            sizeBytes = 24L * 1024 * 1024
        )
    }

    /**
     * 模拟下载 APK，进度推送到 [downloadProgress]。
     */
    suspend fun download(): String = withContext(Dispatchers.IO) {
        for (i in 0..100 step 5) {
            _downloadProgress.value = i
            delay(80)
        }
        "fake_path/aiai_1.2.0.apk"
    }

    companion object {
        @Volatile
        private var instance: UpdateChecker? = null

        fun get(context: Context): UpdateChecker {
            return instance ?: synchronized(this) {
                instance ?: UpdateChecker(context.applicationContext).also { instance = it }
            }
        }
    }
}
