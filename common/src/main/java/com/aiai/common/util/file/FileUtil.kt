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
package com.aiai.common.util.file

import android.content.Context
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.text.DecimalFormat

/**
 * 文件操作工具类。
 *
 * 提供读写、复制、删除、大小格式化、目录管理等基础能力。
 */
object FileUtil {

    /** 读取文件为字符串。 */
    fun readFile(file: File): String? {
        return try {
            if (!file.exists()) return null
            BufferedReader(InputStreamReader(FileInputStream(file), Charsets.UTF_8)).use { it.readText() }
        } catch (e: IOException) {
            null
        }
    }

    /** 写入字符串到文件。 */
    fun writeFile(file: File, content: String): Boolean {
        return try {
            file.parentFile?.mkdirs()
            FileOutputStream(file).use { it.write(content.toByteArray(Charsets.UTF_8)) }
            true
        } catch (e: IOException) {
            false
        }
    }

    /** 复制文件。 */
    fun copy(src: File, dest: File): Boolean {
        return try {
            dest.parentFile?.mkdirs()
            FileInputStream(src).use { input ->
                FileOutputStream(dest).use { output ->
                    input.copyTo(output)
                }
            }
            true
        } catch (e: IOException) {
            false
        }
    }

    /** 删除文件或目录（递归）。 */
    fun delete(file: File): Boolean {
        return try {
            if (file.isDirectory) file.listFiles()?.forEach { delete(it) }
            file.delete()
        } catch (e: Exception) {
            false
        }
    }

    /** 格式化文件大小。 */
    fun formatSize(bytes: Long): String {
        if (bytes <= 0) return "0 B"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        var s = bytes.toDouble()
        var i = 0
        while (s >= 1024 && i < units.size - 1) { s /= 1024; i++ }
        return DecimalFormat("#,##0.#").format(s) + " " + units[i]
    }

    /** 获取目录大小（递归）。 */
    fun dirSize(dir: File): Long {
        if (!dir.exists()) return 0
        if (dir.isFile) return dir.length()
        var size = 0L
        dir.listFiles()?.forEach { size += dirSize(it) }
        return size
    }

    /** 获取应用缓存目录。 */
    fun getCacheDir(context: Context): File = context.cacheDir

    /** 获取应用外部缓存目录。 */
    fun getExternalCacheDir(context: Context): File? = context.externalCacheDir

    /** 获取应用文件目录。 */
    fun getFilesDir(context: Context): File = context.filesDir
}
