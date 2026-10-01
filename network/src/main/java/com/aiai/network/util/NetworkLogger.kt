/*
 * Copyright (c) 2024 爱Ai (AiAi) App. All rights reserved.
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
package com.aiai.network.util

import android.util.Log
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 网络日志记录器。
 *
 * 将网络请求日志保存到文件，便于调试和分析。
 */
class NetworkLogger(
    private val logDir: File,
    private val maxLogSize: Long = 10L * 1024L * 1024L,
) {

    companion object {
        private const val TAG = "NetworkLogger"
        private const val LOG_FILE_PREFIX = "network_"
        private const val LOG_FILE_SUFFIX = ".log"
    }

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US)

    init {
        logDir.mkdirs()
    }

    /**
     * 记录请求日志。
     *
     * @param url 请求URL
     * @param method 请求方法
     * @param headers 请求头
     * @param body 请求体
     * @param durationMs 耗时
     * @param responseCode 响应码
     */
    fun logRequest(
        url: String,
        method: String,
        headers: Map<String, String>,
        body: String?,
        durationMs: Long,
        responseCode: Int,
    ) {
        val logEntry = buildString {
            append("[").append(dateFormat.format(Date())).append("] ")
            append(method).append(" ").append(url).append(" ")
            append(responseCode).append(" ").append(durationMs).append("ms\n")
            append("Headers: ").append(headers).append("\n")
            if (!body.isNullOrBlank()) {
                append("Body: ").append(body.take(500)).append("\n")
            }
            append("---\n")
        }

        writeToFile(logEntry)
        Log.d(TAG, logEntry)
    }

    /**
     * 记录错误日志。
     *
     * @param url 请求URL
     * @param error 错误信息
     */
    fun logError(url: String, error: String) {
        val logEntry = buildString {
            append("[").append(dateFormat.format(Date())).append("] ERROR ")
            append(url).append(": ").append(error).append("\n---\n")
        }
        writeToFile(logEntry)
        Log.e(TAG, logEntry)
    }

    /**
     * 写入日志文件。
     */
    private fun writeToFile(content: String) {
        try {
            val logFile = getCurrentLogFile()
            // 检查文件大小
            if (logFile.exists() && logFile.length() > maxLogSize) {
                rotateLog(logFile)
            }
            FileWriter(logFile, true).use { it.append(content) }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to write log: ${e.message}")
        }
    }

    /**
     * 获取当前日志文件。
     */
    private fun getCurrentLogFile(): File {
        val date = SimpleDateFormat("yyyyMMdd", Locale.US).format(Date())
        return File(logDir, "$LOG_FILE_PREFIX$date$LOG_FILE_SUFFIX")
    }

    /**
     * 日志轮转。
     */
    private fun rotateLog(oldFile: File) {
        val archiveName = oldFile.nameWithoutExtension + "_${System.currentTimeMillis()}.log"
        oldFile.renameTo(File(logDir, archiveName))
        // 清理过期日志
        cleanOldLogs()
    }

    /**
     * 清理旧日志文件。
     */
    private fun cleanOldLogs() {
        val files = logDir.listFiles { f -> f.name.startsWith(LOG_FILE_PREFIX) } ?: return
        if (files.size > 7) {
            files.sortedBy { it.lastModified() }
                .take(files.size - 7)
                .forEach { it.delete() }
        }
    }

    /**
     * 清除所有日志。
     */
    fun clearAll() {
        logDir.listFiles()?.forEach { it.delete() }
    }

    /**
     * 获取日志文件列表。
     */
    fun getLogFiles(): List<File> {
        return logDir.listFiles()?.sortedByDescending { it.lastModified() } ?: emptyList()
    }
}
