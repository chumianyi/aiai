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
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * AES 加解密工具类。
 *
 * 支持 CBC / ECB / GCM 三种模式，密钥长度 128/192/256 位。
 * - CBC：需 IV，安全性高，推荐
 * - ECB：无需 IV，安全性低，仅兼容旧数据
 * - GCM：带认证，提供完整性校验
 */
object AESUtil {

    private const val ALGORITHM = "AES"
    private const val TRANSFORM_CBC = "AES/CBC/PKCS5Padding"
    private const val TRANSFORM_ECB = "AES/ECB/PKCS5Padding"
    private const val TRANSFORM_GCM = "AES/GCM/NoPadding"
    private const val IV_LENGTH = 16
    private const val GCM_TAG_LENGTH = 128

    /** 生成随机密钥（Base64 编码）。 */
    fun generateKey(keySize: Int = 256): String {
        val key = ByteArray(keySize / 8)
        SecureRandom().nextBytes(key)
        return Base64.encodeToString(key, Base64.NO_WRAP)
    }

    /** 生成随机 IV。 */
    fun generateIv(): ByteArray {
        return ByteArray(IV_LENGTH).also { SecureRandom().nextBytes(it) }
    }

    // region CBC 模式

    /**
     * CBC 模式加密，返回 Base64(IV + 密文)。
     */
    fun encryptCbc(plainText: String, key: String): String {
        val keyBytes = Base64.decode(key, Base64.NO_WRAP)
        val iv = generateIv()
        val cipher = Cipher.getInstance(TRANSFORM_CBC)
        cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(keyBytes, ALGORITHM), IvParameterSpec(iv))
        val encrypted = cipher.doFinal(plainText.toByteArray(StandardCharsets.UTF_8))
        return Base64.encodeToString(iv + encrypted, Base64.NO_WRAP)
    }

    /**
     * CBC 模式解密，输入 Base64(IV + 密文)。
     */
    fun decryptCbc(cipherText: String, key: String): String {
        val keyBytes = Base64.decode(key, Base64.NO_WRAP)
        val data = Base64.decode(cipherText, Base64.NO_WRAP)
        val iv = data.copyOfRange(0, IV_LENGTH)
        val encrypted = data.copyOfRange(IV_LENGTH, data.size)
        val cipher = Cipher.getInstance(TRANSFORM_CBC)
        cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(keyBytes, ALGORITHM), IvParameterSpec(iv))
        return String(cipher.doFinal(encrypted), StandardCharsets.UTF_8)
    }

    // endregion

    // region ECB 模式

    /** ECB 模式加密。 */
    fun encryptEcb(plainText: String, key: String): String {
        val keyBytes = Base64.decode(key, Base64.NO_WRAP)
        val cipher = Cipher.getInstance(TRANSFORM_ECB)
        cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(keyBytes, ALGORITHM))
        return Base64.encodeToString(cipher.doFinal(plainText.toByteArray(StandardCharsets.UTF_8)), Base64.NO_WRAP)
    }

    /** ECB 模式解密。 */
    fun decryptEcb(cipherText: String, key: String): String {
        val keyBytes = Base64.decode(key, Base64.NO_WRAP)
        val cipher = Cipher.getInstance(TRANSFORM_ECB)
        cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(keyBytes, ALGORITHM))
        return String(cipher.doFinal(Base64.decode(cipherText, Base64.NO_WRAP)), StandardCharsets.UTF_8)
    }

    // endregion

    // region GCM 模式

    /**
     * GCM 模式加密，返回 Base64(IV + 密文 + Tag)。
     */
    fun encryptGcm(plainText: String, key: String): String {
        val keyBytes = Base64.decode(key, Base64.NO_WRAP)
        val iv = generateIv()
        val cipher = Cipher.getInstance(TRANSFORM_GCM)
        cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(keyBytes, ALGORITHM), GCMParameterSpec(GCM_TAG_LENGTH, iv))
        val encrypted = cipher.doFinal(plainText.toByteArray(StandardCharsets.UTF_8))
        return Base64.encodeToString(iv + encrypted, Base64.NO_WRAP)
    }

    /**
     * GCM 模式解密。
     */
    fun decryptGcm(cipherText: String, key: String): String {
        val keyBytes = Base64.decode(key, Base64.NO_WRAP)
        val data = Base64.decode(cipherText, Base64.NO_WRAP)
        val iv = data.copyOfRange(0, IV_LENGTH)
        val encrypted = data.copyOfRange(IV_LENGTH, data.size)
        val cipher = Cipher.getInstance(TRANSFORM_GCM)
        cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(keyBytes, ALGORITHM), GCMParameterSpec(GCM_TAG_LENGTH, iv))
        return String(cipher.doFinal(encrypted), StandardCharsets.UTF_8)
    }

    // endregion
}
