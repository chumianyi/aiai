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

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import java.security.SecureRandom

/**
 * 加密工具类。
 *
 * 提供AES加解密、密钥派生等功能。
 */
object CryptoUtils {

    private const val ALGORITHM_AES = "AES"
    private const val TRANSFORMATION_AES = "AES/CBC/PKCS5Padding"
    private const val KEY_DERIVATION = "PBKDF2WithHmacSHA256"
    private const val SALT_LENGTH = 16
    private const val IV_LENGTH = 16
    private const val KEY_LENGTH = 256
    private const val ITERATIONS = 10000

    private val secureRandom = SecureRandom()

    /**
     * 使用密码派生AES密钥。
     *
     * @param password 密码
     * @param salt 盐值
     * @return AES密钥
     */
    fun deriveKey(password: String, salt: ByteArray): SecretKeySpec {
        val spec = PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH)
        val factory = SecretKeyFactory.getInstance(KEY_DERIVATION)
        val keyBytes = factory.generateSecret(spec).encoded
        return SecretKeySpec(keyBytes, ALGORITHM_AES)
    }

    /**
     * 生成随机盐值。
     *
     * @return 随机盐值字节数组
     */
    fun generateSalt(): ByteArray {
        return ByteArray(SALT_LENGTH).apply { secureRandom.nextBytes(this) }
    }

    /**
     * 生成随机IV。
     *
     * @return 随机IV字节数组
     */
    fun generateIv(): ByteArray {
        return ByteArray(IV_LENGTH).apply { secureRandom.nextBytes(this) }
    }

    /**
     * AES加密。
     *
     * @param data 明文数据
     * @param key 密钥
     * @param iv IV向量
     * @return 加密后的Base64字符串
     */
    fun encrypt(data: String, key: ByteArray, iv: ByteArray): String {
        val cipher = Cipher.getInstance(TRANSFORMATION_AES)
        val keySpec = SecretKeySpec(key, ALGORITHM_AES)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, javax.crypto.spec.IvParameterSpec(iv))
        val encrypted = cipher.doFinal(data.toByteArray())
        return Base64.encodeToString(encrypted, Base64.NO_WRAP)
    }

    /**
     * AES解密。
     *
     * @param encryptedData Base64编码的密文
     * @param key 密钥
     * @param iv IV向量
     * @return 明文字符串
     */
    fun decrypt(encryptedData: String, key: ByteArray, iv: ByteArray): String {
        val cipher = Cipher.getInstance(TRANSFORMATION_AES)
        val keySpec = SecretKeySpec(key, ALGORITHM_AES)
        cipher.init(Cipher.DECRYPT_MODE, keySpec, javax.crypto.spec.IvParameterSpec(iv))
        val decoded = Base64.decode(encryptedData, Base64.NO_WRAP)
        val decrypted = cipher.doFinal(decoded)
        return String(decrypted, Charsets.UTF_8)
    }

    /**
     * 生成随机字符串。
     *
     * @param length 长度
     * @return 随机字符串
     */
    fun randomString(length: Int): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length).map { chars[secureRandom.nextInt(chars.length)] }.joinToString("")
    }

    /**
     * Base64编码。
     *
     * @param data 原始字节
     * @return Base64字符串
     */
    fun base64Encode(data: ByteArray): String {
        return Base64.encodeToString(data, Base64.NO_WRAP)
    }

    /**
     * Base64解码。
     *
     * @param data Base64字符串
     * @return 原始字节
     */
    fun base64Decode(data: String): ByteArray {
        return Base64.decode(data, Base64.NO_WRAP)
    }
}
