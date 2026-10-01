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
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.Security
import java.security.Signature
import java.security.spec.ECGenParameterSpec

/**
 * 国密 SM2 非对称加密工具类（简化版）。
 *
 * SM2 基于椭圆曲线密码学（ECC），用于数字签名和密钥交换。
 * 基于 BouncyCastle 实现。
 */
object SM2Util {

    private const val ALGORITHM = "EC"
    private const val CURVE = "sm2p256v1"
    private const val SIGN_ALGORITHM = "SHA256withSM2"

    init {
        Security.addProvider(BouncyCastleProvider())
    }

    /** 生成 SM2 密钥对。 */
    fun generateKeyPair(): KeyPair {
        val generator = KeyPairGenerator.getInstance(ALGORITHM, BouncyCastleProvider.PROVIDER_NAME)
        generator.initialize(ECGenParameterSpec(CURVE))
        return generator.generateKeyPair()
    }

    /** 获取公钥 Base64。 */
    fun publicKeyToBase64(keyPair: KeyPair): String {
        return Base64.encodeToString(keyPair.public.encoded, Base64.NO_WRAP)
    }

    /** 获取私钥 Base64。 */
    fun privateKeyToBase64(keyPair: KeyPair): String {
        return Base64.encodeToString(keyPair.private.encoded, Base64.NO_WRAP)
    }

    /**
     * 签名（简化：使用私钥字节直接签）。
     */
    fun sign(data: String, privateKeyBytes: ByteArray): String {
        val keyFactory = java.security.KeyFactory.getInstance(ALGORITHM, BouncyCastleProvider.PROVIDER_NAME)
        val privateKey = keyFactory.generatePrivate(java.security.spec.PKCS8EncodedKeySpec(privateKeyBytes))
        val signature = Signature.getInstance(SIGN_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME)
        signature.initSign(privateKey)
        signature.update(data.toByteArray(StandardCharsets.UTF_8))
        return Base64.encodeToString(signature.sign(), Base64.NO_WRAP)
    }

    /**
     * 验签。
     */
    fun verify(data: String, signBase64: String, publicKeyBytes: ByteArray): Boolean {
        return try {
            val keyFactory = java.security.KeyFactory.getInstance(ALGORITHM, BouncyCastleProvider.PROVIDER_NAME)
            val publicKey = keyFactory.generatePublic(java.security.spec.X509EncodedKeySpec(publicKeyBytes))
            val signature = Signature.getInstance(SIGN_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME)
            signature.initVerify(publicKey)
            signature.update(data.toByteArray(StandardCharsets.UTF_8))
            signature.verify(Base64.decode(signBase64, Base64.NO_WRAP))
        } catch (e: Exception) {
            false
        }
    }
}
