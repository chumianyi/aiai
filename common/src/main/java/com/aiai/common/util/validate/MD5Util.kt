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

import java.security.MessageDigest

/**
 * MD5 散列工具类。
 *
 * 注意：MD5 已被证明不安全，仅用于数据校验，不可用于密码存储。
 */
object MD5Util {

    /** 计算字符串的 MD5（32 位小写）。 */
    fun md5(text: String): String {
        val digest = MessageDigest.getInstance("MD5").digest(text.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }

    /** 计算字节数组的 MD5。 */
    fun md5Bytes(data: ByteArray): String {
        val digest = MessageDigest.getInstance("MD5").digest(data)
        return digest.joinToString("") { "%02x".format(it) }
    }

    /** 计算文件的 MD5。 */
    fun md5File(filePath: String): String? {
        return try {
            val file = java.io.File(filePath)
            if (!file.exists()) return null
            val digest = MessageDigest.getInstance("MD5")
            java.io.FileInputStream(file).use { input ->
                val buffer = ByteArray(8192)
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    digest.update(buffer, 0, read)
                }
            }
            digest.digest().joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            null
        }
    }
}
