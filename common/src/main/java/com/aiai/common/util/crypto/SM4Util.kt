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
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.nio.charset.StandardCharsets
import java.security.Security
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * 国密 SM4 对称加密工具类。
 *
 * SM4 是中国国家密码管理局发布的分组密码算法，分组长度和密钥长度均为 128 位。
 * 基于 BouncyCastle 实现 ECB/PKCS5Padding 模式。
 */
object SM4Util {

    private const val ALGORITHM = "SM4"
    private const val TRANSFORM = "SM4/ECB/PKCS5Padding"

    init {
        Security.addProvider(BouncyCastleProvider())
    }

    /**
     * SM4 加密。
     *
     * @param plainText 明文
     * @param key 16 字节密钥（UTF-8 字符串）
     * @return Base64 编码密文
     */
    fun encrypt(plainText: String, key: String): String {
        val keyBytes = key.toByteArray(StandardCharsets.UTF_8)
        val cipher = Cipher.getInstance(TRANSFORM, BouncyCastleProvider.PROVIDER_NAME)
        cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(keyBytes, ALGORITHM))
        return Base64.encodeToString(cipher.doFinal(plainText.toByteArray(StandardCharsets.UTF_8)), Base64.NO_WRAP)
    }

    /**
     * SM4 解密。
     *
     * @param cipherText Base64 编码密文
     * @param key 16 字节密钥
     */
    fun decrypt(cipherText: String, key: String): String {
        val keyBytes = key.toByteArray(StandardCharsets.UTF_8)
        val cipher = Cipher.getInstance(TRANSFORM, BouncyCastleProvider.PROVIDER_NAME)
        cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(keyBytes, ALGORITHM))
        return String(cipher.doFinal(Base64.decode(cipherText, Base64.NO_WRAP)), StandardCharsets.UTF_8)
    }
}
