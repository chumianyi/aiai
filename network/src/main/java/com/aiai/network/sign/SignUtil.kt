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

import java.net.URLEncoder

/**
 * 签名工具类。
 *
 * 提供参数排序、拼接、URL编码等签名辅助功能。
 */
object SignUtil {

    /**
     * 将参数Map按key字典序排序后拼接为签名字符串。
     *
     * 格式：key1=value1&key2=value2&key3=value3
     *
     * @param params 参数Map
     * @return 排序拼接后的签名字符串
     */
    fun buildSignString(params: Map<String, String>): String {
        return params.entries
            .filter { it.value.isNotBlank() }
            .sortedBy { it.key }
            .joinToString("&") { "${it.key}=${it.value}" }
    }

    /**
     * 将参数Map按key字典序排序后拼接为签名字符串（排除空值和指定key）。
     *
     * @param params 参数Map
     * @param excludeKeys 要排除的key列表
     * @return 排序拼接后的签名字符串
     */
    fun buildSignStringExclude(params: Map<String, String>, excludeKeys: Set<String>): String {
        return params.entries
            .filter { it.value.isNotBlank() && it.key !in excludeKeys }
            .sortedBy { it.key }
            .joinToString("&") { "${it.key}=${it.value}" }
    }

    /**
     * URL编码字符串。
     *
     * @param value 原始字符串
     * @return 编码后的字符串
     */
    fun urlEncode(value: String): String {
        return URLEncoder.encode(value, "UTF-8")
    }

    /**
     * 生成随机nonce字符串。
     *
     * @param length 长度，默认16
     * @return 随机字符串
     */
    fun generateNonce(length: Int = 16): String {
        val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..length)
            .map { chars.random() }
            .joinToString("")
    }

    /**
     * 获取当前时间戳（秒）。
     *
     * @return 当前Unix时间戳
     */
    fun currentTimestamp(): Long = System.currentTimeMillis() / 1000

    /**
     * 生成完整的签名头Map。
     *
     * @param path 请求路径
     * @param method 请求方法
     * @param body 请求体
     * @param secret 签名密钥
     * @return 签名头Map
     */
    fun buildSignHeaders(
        path: String,
        method: String,
        body: String,
        secret: String,
    ): Map<String, String> {
        val timestamp = currentTimestamp().toString()
        val nonce = generateNonce()
        val signSource = "$timestamp$nonce$method$path$body"
        val signature = HmacSigner().sign(signSource, secret)

        return mapOf(
            "X-Timestamp" to timestamp,
            "X-Nonce" to nonce,
            "X-Signature" to signature,
        )
    }
}
