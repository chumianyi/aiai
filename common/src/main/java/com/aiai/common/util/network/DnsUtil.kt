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
package com.aiai.common.util.network

import java.net.InetAddress

/**
 * DNS 解析工具类。
 */
object DnsUtil {

    /** 解析域名到 IP 地址列表。 */
    fun lookup(host: String): List<String> {
        return try {
            InetAddress.getAllByName(host).map { it.hostAddress ?: "" }.filter { it.isNotEmpty() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    /** 反向解析 IP 到域名。 */
    fun reverseLookup(ip: String): String? {
        return try {
            InetAddress.getByName(ip).canonicalHostName
        } catch (e: Exception) {
            null
        }
    }
}
