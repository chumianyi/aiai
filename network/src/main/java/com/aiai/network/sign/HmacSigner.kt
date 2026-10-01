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
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * HMAC-SHA256签名实现。
 *
 * 使用HMAC-SHA256算法对请求数据进行签名。
 */
class HmacSigner : Signer {

    companion object {
        private const val HMAC_SHA256 = "HmacSHA256"
    }

    override fun sign(data: String, secret: String): String {
        val mac = Mac.getInstance(HMAC_SHA256)
        val keySpec = SecretKeySpec(secret.toByteArray(), HMAC_SHA256)
        mac.init(keySpec)
        val hash = mac.doFinal(data.toByteArray())
        return Base64.encodeToString(hash, Base64.NO_WRAP)
    }

    override fun verify(data: String, signature: String, secret: String): Boolean {
        val expected = sign(data, secret)
        return expected == signature
    }

    /**
     * 使用Hex编码签名。
     *
     * @param data 待签名数据
     * @param secret 密钥
     * @return Hex编码的签名
     */
    fun signHex(data: String, secret: String): String {
        val mac = Mac.getInstance(HMAC_SHA256)
        val keySpec = SecretKeySpec(secret.toByteArray(), HMAC_SHA256)
        mac.init(keySpec)
        val hash = mac.doFinal(data.toByteArray())
        return hash.joinToString("") { "%02x".format(it) }
    }
}
