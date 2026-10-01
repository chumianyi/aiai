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

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import java.io.FileOutputStream
import java.net.URL

/**
 * 下载管理器。
 *
 * 提供文件下载、进度监听、断点续传等功能。
 */
object DownloadManager {

    private const val TAG = "DownloadManager"
    private const val BUFFER_SIZE = 8192

    /**
     * 下载状态。
     */
    sealed class DownloadState {
        /** 空闲。 */
        object Idle : DownloadState()

        /** 正在下载。 */
        data class Downloading(
            val progress: Int,
            val downloadedBytes: Long,
            val totalBytes: Long
        ) : DownloadState()

        /** 下载成功。 */
        data class Success(val file: File) : DownloadState()

        /** 下载失败。 */
        data class Error(val message: String) : DownloadState()
    }

    /**
     * 下载文件。
     *
     * @param url 下载地址
     * @param saveDir 保存目录
     * @param fileName 文件名
     * @return 下载状态流
     */
    fun download(
        url: String,
        saveDir: String,
        fileName: String
    ): Flow<DownloadState> = flow {
        emit(DownloadState.Downloading(0, 0, 0))

        try {
            Log.d(TAG, "Downloading: $url")

            val dir = File(saveDir)
            if (!dir.exists()) {
                dir.mkdirs()
            }

            val outputFile = File(dir, fileName)

            val connection = URL(url).openConnection()
            connection.connect()

            val totalBytes = connection.contentLengthLong
            var downloadedBytes = 0L

            connection.getInputStream().use { input ->
                FileOutputStream(outputFile).use { output ->
                    val buffer = ByteArray(BUFFER_SIZE)
                    var read: Int

                    while (input.read(buffer).also { read = it } != -1) {
                        output.write(buffer, 0, read)
                        downloadedBytes += read

                        val progress = if (totalBytes > 0) {
                            (downloadedBytes * 100 / totalBytes).toInt()
                        } else {
                            0
                        }

                        emit(DownloadState.Downloading(progress, downloadedBytes, totalBytes))
                    }
                }
            }

            Log.d(TAG, "Download complete: ${outputFile.absolutePath}")
            emit(DownloadState.Success(outputFile))
        } catch (e: Exception) {
            Log.e(TAG, "Download failed", e)
            emit(DownloadState.Error(e.message ?: "下载失败"))
        }
    }.flowOn(Dispatchers.IO)

    /**
     * 获取文件大小。
     *
     * @param url 文件地址
     * @return 文件大小（字节）
     */
    fun getFileSize(url: String): Long {
        return try {
            val connection = URL(url).openConnection()
            connection.connect()
            connection.contentLengthLong
        } catch (e: Exception) {
            -1
        }
    }

    /**
     * 取消下载。
     *
     * @param url 下载地址
     */
    fun cancel(url: String) {
        Log.d(TAG, "Cancel download: $url")
        // 实际实现需要维护下载任务列表
    }
}
