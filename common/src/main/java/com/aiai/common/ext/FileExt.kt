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
package com.aiai.common.ext

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.text.DecimalFormat

/**
 * File 相关扩展函数集合。
 *
 * 提供读写、复制移动删除、大小格式化、MIME 类型、目录创建、媒体扫描等能力。
 */

// region 读写

/** 读取文件内容为字符串（UTF-8）。 */
fun File.readTextOrDefault(default: String = ""): String {
    return try {
        if (!exists()) return default
        BufferedReader(InputStreamReader(FileInputStream(this), Charsets.UTF_8)).use { it.readText() }
    } catch (e: IOException) {
        default
    }
}

/** 写入字符串到文件（覆盖）。 */
fun File.writeText(text: String): Boolean {
    return try {
        parentFile?.mkdirs()
        FileOutputStream(this).use { it.write(text.toByteArray(Charsets.UTF_8)) }
        true
    } catch (e: IOException) {
        false
    }
}

/** 追加字符串到文件末尾。 */
fun File.appendText(text: String): Boolean {
    return try {
        parentFile?.mkdirs()
        FileOutputStream(this, true).use { it.write(text.toByteArray(Charsets.UTF_8)) }
        true
    } catch (e: IOException) {
        false
    }
}

// endregion

// region 复制移动删除

/** 复制文件到 [dest]，覆盖已存在文件。 */
fun File.copyTo(dest: File, overwrite: Boolean = true): Boolean {
    return try {
        if (!exists()) return false
        if (dest.exists() && !overwrite) return false
        dest.parentFile?.mkdirs()
        inputStream().use { input ->
            FileOutputStream(dest).use { output ->
                input.copyTo(output)
            }
        }
        true
    } catch (e: IOException) {
        false
    }
}

/** 移动文件到 [dest]。 */
fun File.moveTo(dest: File): Boolean {
    return if (copyTo(dest)) {
        delete()
        true
    } else false
}

/** 安全删除文件或目录（递归）。 */
fun File.deleteRecursivelySafe(): Boolean {
    return try {
        if (isDirectory) listFiles()?.forEach { it.deleteRecursivelySafe() }
        delete()
    } catch (e: Exception) {
        false
    }
}

// endregion

// region 大小 & MIME

/** 文件大小格式化为易读字符串（B/KB/MB/GB）。 */
fun File.readableSize(): String {
    val size = length()
    if (size <= 0) return "0 B"
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    var digit = 0
    var s = size.toDouble()
    while (s >= 1024 && digit < units.size - 1) {
        s /= 1024
        digit++
    }
    return DecimalFormat("#,##0.#").format(s) + " " + units[digit]
}

/** 根据扩展名获取 MIME 类型。 */
fun File.mimeType(): String {
    return when (extension.lowercase()) {
        "jpg", "jpeg" -> "image/jpeg"
        "png" -> "image/png"
        "gif" -> "image/gif"
        "webp" -> "image/webp"
        "mp4" -> "video/mp4"
        "mp3" -> "audio/mpeg"
        "wav" -> "audio/wav"
        "pdf" -> "application/pdf"
        "txt" -> "text/plain"
        "json" -> "application/json"
        "zip" -> "application/zip"
        else -> "application/octet-stream"
    }
}

// endregion

// region 目录 & 媒体扫描

/** 确保目录存在（不存在则创建）。 */
fun File.ensureDir(): File {
    if (!exists()) mkdirs()
    return this
}

/** 确保文件父目录存在。 */
fun File.ensureParentDir(): File {
    parentFile?.mkdirs()
    return this
}

/** 将文件扫描进系统媒体库（相册可见）。 */
fun File.scanMedia(context: Context) {
    MediaScannerConnection.scanFile(context, arrayOf(absolutePath), null, null)
}

/** 获取文件的 Uri（兼容 Android 7+ FileProvider）。 */
fun File.toUri(context: Context): Uri {
    return androidx.core.content.FileProvider.getUriForFile(
        context, "${context.packageName}.fileprovider", this
    )
}

// endregion
