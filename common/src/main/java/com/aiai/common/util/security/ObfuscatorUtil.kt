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
package com.aiai.common.util.security

import android.util.Base64
import com.aiai.common.util.crypto.AESUtil

/**
 * 字符串混淆工具类。
 *
 * 防止硬编码字符串被反编译直接看到。
 * 使用 AES 加密 + Base64 编码。
 */
object ObfuscatorUtil {

    private const val DEFAULT_KEY = "aiai_default_key_16"

    /** 混淆字符串：加密后 Base64 编码。 */
    fun obfuscate(plainText: String): String {
        return try {
            AESUtil.encryptEcb(plainText, DEFAULT_KEY)
        } catch (e: Exception) {
            ""
        }
    }

    /** 反混淆字符串。 */
    fun deobfuscate(obfuscated: String): String {
        return try {
            AESUtil.decryptEcb(obfuscated, DEFAULT_KEY)
        } catch (e: Exception) {
            obfuscated
        }
    }
}
