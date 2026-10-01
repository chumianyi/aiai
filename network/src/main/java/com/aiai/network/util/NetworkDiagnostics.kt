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

import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * 网络诊断工具。
 *
 * 提供网络连通性检测、延迟测试等功能。
 */
object NetworkDiagnostics {

    private const val TAG = "NetworkDiagnostics"
    private const val DEFAULT_TIMEOUT = 5_000

    /**
     * Ping测试，测量到指定URL的延迟。
     *
     * @param url 测试URL
     * @param timeoutMs 超时毫秒数
     * @return 延迟毫秒数，-1表示失败
     */
    fun ping(url: String, timeoutMs: Int = DEFAULT_TIMEOUT): Long {
        val startTime = System.nanoTime()
        var connection: HttpURLConnection? = null
        return try {
            connection = (URL(url).openConnection() as HttpURLConnection).apply {
                connectTimeout = timeoutMs
                readTimeout = timeoutMs
                requestMethod = "HEAD"
                connect()
            }
            val endTime = System.nanoTime()
            (endTime - startTime) / 1_000_000
        } catch (e: Exception) {
            Log.w(TAG, "Ping failed: ${e.message}")
            -1
        } finally {
            connection?.disconnect()
        }
    }

    /**
     * 测试DNS解析时间。
     *
     * @param hostname 域名
     * @return 解析耗时毫秒数，-1表示失败
     */
    fun dnsLookupTime(hostname: String): Long {
        return try {
            val start = System.nanoTime()
            val addresses = java.net.InetAddress.getAllByName(hostname)
            val end = System.nanoTime()
            if (addresses.isNotEmpty()) (end - start) / 1_000_000 else -1
        } catch (e: Exception) {
            -1
        }
    }

    /**
     * 批量测试多个URL的延迟。
     *
     * @param urls URL列表
     * @return URL→延迟的Map
     */
    fun batchPing(urls: List<String>): Map<String, Long> {
        val result = mutableMapOf<String, Long>()
        urls.forEach { url ->
            result[url] = ping(url)
        }
        return result
    }

    /**
     * 检测最佳服务器节点。
     *
     * @param nodes 服务器节点URL列表
     * @return 延迟最低的节点URL
     */
    fun findBestNode(nodes: List<String>): String? {
        if (nodes.isEmpty()) return null
        return batchPing(nodes)
            .filterValues { it > 0 }
            .minByOrNull { it.value }
            ?.key
    }

    /**
     * 获取网络诊断报告。
     *
     * @return 诊断报告字符串
     */
    fun getDiagnosticReport(): String {
        val report = StringBuilder()
        report.appendLine("=== Network Diagnostic Report ===")
        report.appendLine("Time: ${System.currentTimeMillis()}")
        report.appendLine("---")
        // DNS解析测试
        val dnsTime = dnsLookupTime("api.aiai.com")
        report.appendLine("DNS Lookup: ${if (dnsTime > 0) "${dnsTime}ms" else "Failed"}")
        // Ping测试
        val pingTime = ping("https://api.aiai.com/")
        report.appendLine("Ping: ${if (pingTime > 0) "${pingTime}ms" else "Failed"}")
        report.appendLine("=== End Report ===")
        return report.toString()
    }
}
