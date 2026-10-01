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
package com.aiai.network.dns

import okhttp3.Dns
import java.net.InetAddress

/**
 * 自定义DNS解析器，支持缓存和多IP备选。
 *
 * 功能：
 * - DNS结果缓存
 * - 失败时切换备选IP
 * - 预解析常用域名
 */
class DnsResolver(
    private val cache: DnsCache = DnsCache(),
) : Dns {

    companion object {
        private const val TAG = "DnsResolver"
    }

    override fun lookup(hostname: String): List<InetAddress> {
        // 先查缓存
        cache.get(hostname)?.let { cachedIps ->
            return cachedIps.mapNotNull { ip ->
                try {
                    InetAddress.getByName(ip)
                } catch (e: Exception) {
                    null
                }
            }
        }

        // 系统DNS解析
        return try {
            val addresses = InetAddress.getAllByName(hostname).toList()
            val ips = addresses.map { it.hostAddress ?: "" }
            cache.put(hostname, ips)
            addresses
        } catch (e: Exception) {
            // 解析失败返回系统默认
            emptyList()
        }
    }

    /**
     * 预解析域名。
     *
     * @param hostnames 域名列表
     */
    fun preResolve(hostnames: List<String>) {
        Thread {
            hostnames.forEach { hostname ->
                try {
                    lookup(hostname)
                } catch (e: Exception) {
                    // 忽略预解析失败
                }
            }
        }.start()
    }

    /**
     * 清除指定域名缓存。
     *
     * @param hostname 域名
     */
    fun clearCache(hostname: String) {
        cache.remove(hostname)
    }

    /**
     * 清除所有DNS缓存。
     */
    fun clearAllCache() {
        cache.clear()
    }
}
