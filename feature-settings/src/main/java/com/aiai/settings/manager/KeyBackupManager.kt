/*
 * Copyright (c) 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.settings.manager

import android.content.Context
import com.aiai.settings.model.ApiConfig
import com.aiai.settings.model.KeyInfo

/**
 * 密钥备份管理：导出/导入加密密钥。
 */
class KeyBackupManager(context: Context) {

    private val encryption = KeyEncryptionManager(context)

    /** 导出所有密钥为加密字符串。 */
    fun export(keys: List<KeyInfo>): String {
        val sb = StringBuilder()
        keys.forEach {
            sb.append(it.alias).append("|")
                .append(encryption.encrypt(it.key)).append("\n")
        }
        return sb.toString()
    }

    /** 从字符串导入密钥。 */
    fun import(raw: String): List<KeyInfo> {
        val result = mutableListOf<KeyInfo>()
        raw.lines().forEach { line ->
            val parts = line.split("|", limit = 2)
            if (parts.size == 2) {
                try {
                    val key = encryption.decrypt(parts[1])
                    result.add(KeyInfo(alias = parts[0], key = key))
                } catch (_: Exception) {}
            }
        }
        return result
    }
}
