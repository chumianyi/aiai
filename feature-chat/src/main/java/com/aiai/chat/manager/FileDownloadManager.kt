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
 * 消息文件下载管理器
 *
 * 管理聊天中文件消息的下载和进度。
 */
class FileDownloadManager(private val context: Context) {

    data class DownloadTask(
        val fileId: String,
        val fileName: String,
        val totalBytes: Long,
        val downloadedBytes: Long,
        val status: DownloadStatus
    )

    enum class DownloadStatus {
        PENDING,
        DOWNLOADING,
        PAUSED,
        COMPLETED,
        FAILED
    }

    private val _downloadTasks = MutableStateFlow<Map<String, DownloadTask>>(emptyMap())
    val downloadTasks: StateFlow<Map<String, DownloadTask>> = _downloadTasks.asStateFlow()

    fun startDownload(fileId: String, fileName: String, totalBytes: Long) {
        val task = DownloadTask(fileId, fileName, totalBytes, 0, DownloadStatus.DOWNLOADING)
        _downloadTasks.value = _downloadTasks.value + (fileId to task)
    }

    fun updateProgress(fileId: String, downloadedBytes: Long) {
        val current = _downloadTasks.value[fileId] ?: return
        _downloadTasks.value = _downloadTasks.value +
                (fileId to current.copy(downloadedBytes = downloadedBytes))
    }

    fun pauseDownload(fileId: String) {
        val current = _downloadTasks.value[fileId] ?: return
        _downloadTasks.value = _downloadTasks.value +
                (fileId to current.copy(status = DownloadStatus.PAUSED))
    }

    fun completeDownload(fileId: String) {
        val current = _downloadTasks.value[fileId] ?: return
        _downloadTasks.value = _downloadTasks.value +
                (fileId to current.copy(status = DownloadStatus.COMPLETED, downloadedBytes = current.totalBytes))
    }

    fun failDownload(fileId: String) {
        val current = _downloadTasks.value[fileId] ?: return
        _downloadTasks.value = _downloadTasks.value +
                (fileId to current.copy(status = DownloadStatus.FAILED))
    }

    fun getTask(fileId: String): DownloadTask? {
        return _downloadTasks.value[fileId]
    }

    fun getDownloadProgress(fileId: String): Float {
        val task = _downloadTasks.value[fileId] ?: return 0f
        return if (task.totalBytes > 0) task.downloadedBytes.toFloat() / task.totalBytes else 0f
    }
}
