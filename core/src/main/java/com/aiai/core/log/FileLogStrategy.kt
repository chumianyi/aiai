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
 * See the License for the specific permissions and
 * limitations under the License.
 */
package com.aiai.core.log

import android.content.Context
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 文件日志策略。
 *
 * 按天切割日志文件，限制最大文件大小。
 */
class FileLogStrategy(
    private val context: Context,
    private val maxSizePerFile: Long = 5L * 1024 * 1024, // 5MB
    private val maxFileCount: Int = 7
) : LogStrategy {

    private val logDir: File = File(context.cacheDir, "logs").apply { mkdirs() }
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())

    override fun log(level: LogLevel, tag: String, message: String, tr: Throwable?) {
        val today = dateFormat.format(Date())
        val file = File(logDir, "log_$today.txt")

        try {
            FileWriter(file, true).use { writer ->
                writer.write("${timeFormat.format(Date())} [${level.name}] $tag: $message\n")
                tr?.let { writer.write("${it.stackTraceToString()}\n") }
            }
            // 检查文件大小，超限则归档
            if (file.length() > maxSizePerFile) {
                archiveFile(file)
            }
        } catch (e: Exception) {
            // 忽略写入失败
        }
    }

    override fun isLoggable(level: LogLevel): Boolean = level.ordinal >= LogLevel.INFO.ordinal

    private fun archiveFile(file: File) {
        val archiveName = file.name.replace(".txt", "_${System.currentTimeMillis()}.txt")
        file.renameTo(File(logDir, archiveName))
        // 清理过期文件
        val files = logDir.listFiles()?.sortedBy { it.lastModified() } ?: return
        if (files.size > maxFileCount) {
            files.take(files.size - maxFileCount).forEach { it.delete() }
        }
    }
}
