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

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL

/**
 * 文件下载工具类。
 *
 * 支持协程异步下载、进度回调。
 */
object DownloadUtil {

    /**
     * 下载文件到指定路径。
     *
     * @param url 下载地址
     * @param destFile 目标文件
     * @param onProgress 进度回调（0-100）
     */
    suspend fun download(
        url: String,
        destFile: File,
        onProgress: (Int) -> Unit = {}
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            destFile.parentFile?.mkdirs()
            val connection = URL(url).openConnection()
            connection.connect()
            val total = connection.contentLengthLong
            var downloaded = 0L
            connection.getInputStream().use { input ->
                FileOutputStream(destFile).use { output ->
                    val buffer = ByteArray(8192)
                    var read: Int
                    while (input.read(buffer).also { read = it } != -1) {
                        output.write(buffer, 0, read)
                        downloaded += read
                        if (total > 0) onProgress((downloaded * 100 / total).toInt())
                    }
                }
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}
