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
package com.aiai.common.util.validate

import java.util.zip.CRC32

/**
 * CRC 循环冗余校验工具类。
 *
 * 用于数据完整性校验，不保证安全性。
 */
object CRCUtil {

    /** 计算字符串的 CRC32 值（Long）。 */
    fun crc32(text: String): Long {
        val crc = CRC32()
        crc.update(text.toByteArray())
        return crc.value
    }

    /** 计算字节数组的 CRC32 值。 */
    fun crc32(data: ByteArray): Long {
        val crc = CRC32()
        crc.update(data)
        return crc.value
    }

    /** 计算文件的 CRC32。 */
    fun crc32File(filePath: String): Long? {
        return try {
            val file = java.io.File(filePath)
            if (!file.exists()) return null
            val crc = CRC32()
            java.io.FileInputStream(file).use { input ->
                val buffer = ByteArray(8192)
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    crc.update(buffer, 0, read)
                }
            }
            crc.value
        } catch (e: Exception) {
            null
        }
    }
}
