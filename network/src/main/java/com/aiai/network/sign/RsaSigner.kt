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
package com.aiai.network.sign

import android.util.Base64
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec

/**
 * RSA签名实现。
 *
 * 使用RSA算法对请求数据进行签名和验签。
 */
class RsaSigner : Signer {

    companion object {
        private const val RSA = "RSA"
        private const val SHA256_WITH_RSA = "SHA256withRSA"
    }

    private var privateKey: PrivateKey? = null
    private var publicKey: PublicKey? = null

    /**
     * 设置私钥（PKCS8格式Base64）。
     *
     * @param base64Key Base64编码的私钥
     */
    fun setPrivateKey(base64Key: String) {
        val keyBytes = Base64.decode(base64Key, Base64.DEFAULT)
        val keySpec = PKCS8EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance(RSA)
        privateKey = keyFactory.generatePrivate(keySpec)
    }

    /**
     * 设置公钥（X509格式Base64）。
     *
     * @param base64Key Base64编码的公钥
     */
    fun setPublicKey(base64Key: String) {
        val keyBytes = Base64.decode(base64Key, Base64.DEFAULT)
        val keySpec = X509EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance(RSA)
        publicKey = keyFactory.generatePublic(keySpec)
    }

    override fun sign(data: String, secret: String): String {
        val key = privateKey ?: throw IllegalStateException("Private key not set")
        val signature = Signature.getInstance(SHA256_WITH_RSA)
        signature.initSign(key)
        signature.update(data.toByteArray())
        val signed = signature.sign()
        return Base64.encodeToString(signed, Base64.NO_WRAP)
    }

    override fun verify(data: String, signature: String, secret: String): Boolean {
        val key = publicKey ?: return false
        return try {
            val sig = Signature.getInstance(SHA256_WITH_RSA)
            sig.initVerify(key)
            sig.update(data.toByteArray())
            val signatureBytes = Base64.decode(signature, Base64.NO_WRAP)
            sig.verify(signatureBytes)
        } catch (e: Exception) {
            false
        }
    }
}
