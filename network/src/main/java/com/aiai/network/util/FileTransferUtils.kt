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
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.security.MessageDigest

/**
 * 文件传输工具。
 *
 * 提供文件复制、校验、分块等功能。
 */
object FileTransferUtils {

    private const val TAG = "FileTransferUtils"
    private const val BUFFER_SIZE = 8192

    /**
     * 复制文件。
     *
     * @param source 源文件
     * @param target 目标文件
     * @return 是否成功
     */
    fun copyFile(source: File, target: File): Boolean {
        return try {
            if (!target.parentFile.exists()) {
                target.parentFile.mkdirs()
            }
            FileInputStream(source).use { input ->
                FileOutputStream(target).use { output ->
                    copyStream(input, output)
                }
            }
            true
        } catch (e: IOException) {
            Log.e(TAG, "Copy file failed: ${e.message}")
            false
        }
    }

    /**
     * 复制流。
     *
     * @param input 输入流
     * @param output 输出流
     */
    fun copyStream(input: InputStream, output: OutputStream) {
        val buffer = ByteArray(BUFFER_SIZE)
        var read: Int
        while (input.read(buffer).also { read = it } != -1) {
            output.write(buffer, 0, read)
        }
        output.flush()
    /**
     * 计算文件MD5。
     *
     * @param file 文件
     * @return MD5哈希字符串
     */
    fun calculateMd5(file: File): String {
        return try {
            val digest = MessageDigest.getInstance("MD5")
            FileInputStream(file).use { input ->
                val buffer = ByteArray(BUFFER_SIZE)
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    digest.update(buffer, 0, read)
                }
            }
            digest.digest().joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            Log.e(TAG, "Calculate MD5 failed: ${e.message}")
            ""
        }
    }

    /**
     * 计算文件SHA256。
     *
     * @param file 文件
     * @return SHA256哈希字符串
     */
    fun calculateSha256(file: File): String {
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            FileInputStream(file).use { input ->
                val buffer = ByteArray(BUFFER_SIZE)
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    digest.update(buffer, 0, read)
                }
            }
            digest.digest().joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            Log.e(TAG, "Calculate SHA256 failed: ${e.message}")
            ""
        }
    }

    /**
     * 验证文件MD5。
     *
     * @param file 文件
     * @param expectedMd5 期望的MD5
     * @return true如果匹配
     */
    fun verifyMd5(file: File, expectedMd5: String): Boolean {
        return calculateMd5(file).equals(expectedMd5, ignoreCase = true)
    }

    /**
     * 获取文件分块列表。
     *
     * @param file 文件
     * @param chunkSize 分块大小（字节）
     * @return 分块列表
     */
    fun getChunks(file: File, chunkSize: Long): List<FileChunk> {
        val chunks = mutableListOf<FileChunk>()
        val totalSize = file.length()
        var offset = 0L
        var index = 0
        while (offset < totalSize) {
            val size = minOf(chunkSize, totalSize - offset)
            chunks.add(FileChunk(index, offset, size))
            offset += size
            index++
        }
        return chunks
    }

    /**
     * 安全删除文件。
     *
     * @param file 文件
     * @return 是否成功
     */
    fun safeDelete(file: File): Boolean {
        return try {
            if (file.exists()) file.delete() else true
        } catch (e: Exception) {
            Log.e(TAG, "Delete failed: ${e.message}")
            false
        }
    }

    /**
     * 获取MIME类型。
     *
     * @param fileName 文件名
     * @return MIME类型
     */
    fun getMimeType(fileName: String): String {
        val ext = fileName.substringAfterLast('.', "").lowercase()
        return when (ext) {
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
            else -> "application/octet-stream"
        }
    }
}

/**
 * 文件分块数据类。
 */
data class FileChunk(
    val index: Int,
    val offset: Long,
    val size: Long,
)
