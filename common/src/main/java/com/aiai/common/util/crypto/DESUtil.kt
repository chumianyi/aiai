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
package com.aiai.common.util.crypto

import android.util.Base64
import java.nio.charset.StandardCharsets
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * DES 对称加密工具类。
 *
 * 注意：DES 密钥长度仅 56 位，安全性较低，仅用于兼容旧系统。
 * 新系统请使用 AES。
 */
@Deprecated("DES 安全性不足，建议使用 AESUtil")
object DESUtil {

    private const val ALGORITHM = "DES"
    private const val TRANSFORM = "DES/CBC/PKCS5Padding"
    private const val IV = "12345678"

    /** DES 加密。 */
    fun encrypt(plainText: String, key: String): String {
        val keyBytes = key.toByteArray(StandardCharsets.UTF_8).copyOf(8)
        val cipher = Cipher.getInstance(TRANSFORM)
        cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(keyBytes, ALGORITHM), IvParameterSpec(IV.toByteArray()))
        return Base64.encodeToString(cipher.doFinal(plainText.toByteArray(StandardCharsets.UTF_8)), Base64.NO_WRAP)
    }

    /** DES 解密。 */
    fun decrypt(cipherText: String, key: String): String {
        val keyBytes = key.toByteArray(StandardCharsets.UTF_8).copyOf(8)
        val cipher = Cipher.getInstance(TRANSFORM)
        cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(keyBytes, ALGORITHM), IvParameterSpec(IV.toByteArray()))
        return String(cipher.doFinal(Base64.decode(cipherText, Base64.NO_WRAP)), StandardCharsets.UTF_8)
    }
}
