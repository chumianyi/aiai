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
package com.aiai.chat.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 文件工具类
 *
 * 提供文件操作、MIME类型检测、大小格式化等工具方法。
 */
object FileUtil {

    /**
     * 获取文件MIME类型
     */
    fun getMimeType(fileName: String): String {
        val extension = MimeTypeMap.getFileExtensionFromUrl(fileName)
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            ?: "application/octet-stream"
    }

    /**
     * 从Uri获取文件名
     */
    fun getFileName(context: Context, uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (index != -1) {
                        result = cursor.getString(index)
                    }
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != -1 && cut != null) {
                result = result?.substring(cut + 1)
            }
        }
        return result
    }

    /**
     * 从Uri获取文件大小
     */
    fun getFileSize(context: Context, uri: Uri): Long {
        var size = 0L
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val index = cursor.getColumnIndex(OpenableColumns.SIZE)
                if (index != -1) {
                    size = cursor.getLong(index)
                }
            }
        }
        return size
    }

    /**
     * 格式化文件大小
     */
    fun formatFileSize(bytes: Long): String {
        return when {
            bytes < 1024 -> "$bytes B"
            bytes < 1024 * 1024 -> String.format("%.1f KB", bytes / 1024.0)
            bytes < 1024 * 1024 * 1024 -> String.format("%.1f MB", bytes / (1024.0 * 1024.0))
            else -> String.format("%.2f GB", bytes / (1024.0 * 1024.0 * 1024.0))
        }
    }

    /**
     * 根据MIME类型判断文件类别
     */
    fun getFileCategory(mimeType: String): String {
        return when {
            mimeType.startsWith("image/") -> "图片"
            mimeType.startsWith("video/") -> "视频"
            mimeType.startsWith("audio/") -> "音频"
            mimeType == "application/pdf" -> "PDF文档"
            mimeType.contains("word") || mimeType.contains("document") -> "Word文档"
            mimeType.contains("excel") || mimeType.contains("spreadsheet") -> "Excel表格"
            mimeType.contains("powerpoint") || mimeType.contains("presentation") -> "PPT"
            mimeType.startsWith("text/") -> "文本文件"
            mimeType.contains("zip") || mimeType.contains("rar") || mimeType.contains("7z") -> "压缩包"
            else -> "其他文件"
        }
    }

    /**
     * 创建临时文件
     */
    fun createTempFile(context: Context, prefix: String, suffix: String): File {
        val dir = File(context.cacheDir, "chat_attachments").apply { mkdirs() }
        return File.createTempFile(prefix, suffix, dir)
    }

    /**
     * 获取时间戳格式化字符串
     */
    fun formatTimestamp(timestamp: Long, pattern: String = "yyyy-MM-dd HH:mm:ss"): String {
        return SimpleDateFormat(pattern, Locale.getDefault()).format(Date(timestamp))
    }

    /**
     * 获取聊天时间显示
     */
    fun formatChatTime(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp
        return when {
            diff < 60_000 -> "刚刚"
            diff < 3_600_000 -> "${diff / 60_000}分钟前"
            isSameDay(timestamp) -> SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(timestamp))
            isYesterday(timestamp) -> "昨天 ${SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(timestamp))}"
            isSameYear(timestamp) -> SimpleDateFormat("MM-dd HH:mm", Locale.getDefault()).format(Date(timestamp))
            else -> SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(timestamp))
        }
    }

    private fun isSameDay(timestamp: Long): Boolean {
        val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        return sdf.format(Date(timestamp)) == sdf.format(Date())
    }

    private fun isYesterday(timestamp: Long): Boolean {
        val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val yesterday = System.currentTimeMillis() - 86_400_000
        return sdf.format(Date(timestamp)) == sdf.format(Date(yesterday))
    }

    private fun isSameYear(timestamp: Long): Boolean {
        val sdf = SimpleDateFormat("yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp)) == sdf.format(Date())
    }
}
