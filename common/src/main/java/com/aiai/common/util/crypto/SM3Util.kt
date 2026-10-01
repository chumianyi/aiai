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

import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.Security

/**
 * 国密 SM3 摘要算法工具类。
 *
 * SM3 是中国国家密码管理局发布的密码杂凑算法，输出 256 位（32 字节）摘要。
 * 基于 BouncyCastle 实现。
 */
object SM3Util {

    private const val ALGORITHM = "SM3"

    init {
        Security.addProvider(BouncyCastleProvider())
    }

    /**
     * 计算 SM3 摘要，返回 Hex 字符串。
     */
    fun digest(data: String): String {
        val md = MessageDigest.getInstance(ALGORITHM, BouncyCastleProvider.PROVIDER_NAME)
        val digest = md.digest(data.toByteArray(StandardCharsets.UTF_8))
        return digest.joinToString("") { "%02x".format(it) }
    }

    /**
     * 计算 SM3 摘要，返回字节数组。
     */
    fun digestBytes(data: String): ByteArray {
        val md = MessageDigest.getInstance(ALGORITHM, BouncyCastleProvider.PROVIDER_NAME)
        return md.digest(data.toByteArray(StandardCharsets.UTF_8))
    }
}
