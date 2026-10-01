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
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

/**
 * RSA 非对称加解密工具类。
 *
 * 支持公钥加密 / 私钥解密、私钥签名 / 公钥验签。
 * 密钥格式为 Base64 编码的 X.509 公钥 / PKCS#8 私钥。
 */
object RSAUtil {

    private const val ALGORITHM = "RSA"
    private const val TRANSFORM = "RSA/ECB/PKCS1Padding"
    private const val SIGN_ALGORITHM = "SHA256withRSA"

    /**
     * 使用公钥加密数据。
     *
     * @param plainText 明文
     * @param publicKeyBase64 Base64 编码的 X.509 公钥
     * @return Base64 编码的密文
     */
    fun encrypt(plainText: String, publicKeyBase64: String): String {
        val keyBytes = Base64.decode(publicKeyBase64, Base64.NO_WRAP)
        val keyFactory = KeyFactory.getInstance(ALGORITHM)
        val publicKey: PublicKey = keyFactory.generatePublic(X509EncodedKeySpec(keyBytes))
        val cipher = Cipher.getInstance(TRANSFORM)
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        return Base64.encodeToString(cipher.doFinal(plainText.toByteArray(StandardCharsets.UTF_8)), Base64.NO_WRAP)
    }

    /**
     * 使用私钥解密数据。
     *
     * @param cipherTextBase64 Base64 编码的密文
     * @param privateKeyBase64 Base64 编码的 PKCS#8 私钥
     */
    fun decrypt(cipherTextBase64: String, privateKeyBase64: String): String {
        val keyBytes = Base64.decode(privateKeyBase64, Base64.NO_WRAP)
        val keyFactory = KeyFactory.getInstance(ALGORITHM)
        val privateKey: PrivateKey = keyFactory.generatePrivate(PKCS8EncodedKeySpec(keyBytes))
        val cipher = Cipher.getInstance(TRANSFORM)
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        return String(cipher.doFinal(Base64.decode(cipherTextBase64, Base64.NO_WRAP)), StandardCharsets.UTF_8)
    }

    /**
     * 使用私钥对数据签名。
     *
     * @return Base64 编码的签名
     */
    fun sign(data: String, privateKeyBase64: String): String {
        val keyBytes = Base64.decode(privateKeyBase64, Base64.NO_WRAP)
        val keyFactory = KeyFactory.getInstance(ALGORITHM)
        val privateKey: PrivateKey = keyFactory.generatePrivate(PKCS8EncodedKeySpec(keyBytes))
        val signature = Signature.getInstance(SIGN_ALGORITHM)
        signature.initSign(privateKey)
        signature.update(data.toByteArray(StandardCharsets.UTF_8))
        return Base64.encodeToString(signature.sign(), Base64.NO_WRAP)
    }

    /**
     * 使用公钥验签。
     *
     * @param data 原始数据
     * @param signBase64 Base64 编码的签名
     * @return 验签是否通过
     */
    fun verify(data: String, signBase64: String, publicKeyBase64: String): Boolean {
        return try {
            val keyBytes = Base64.decode(publicKeyBase64, Base64.NO_WRAP)
            val keyFactory = KeyFactory.getInstance(ALGORITHM)
            val publicKey: PublicKey = keyFactory.generatePublic(X509EncodedKeySpec(keyBytes))
            val signature = Signature.getInstance(SIGN_ALGORITHM)
            signature.initVerify(publicKey)
            signature.update(data.toByteArray(StandardCharsets.UTF_8))
            signature.verify(Base64.decode(signBase64, Base64.NO_WRAP))
        } catch (e: Exception) {
            false
        }
    }
}
