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
package com.aiai.network.util

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.net.Socket

/**
 * API 健康检查器。
 *
 * 提供 ping、延迟、可用性等健康检查功能。
 */
object ApiHealthChecker {

    private const val TAG = "ApiHealthChecker"
    private const val DEFAULT_TIMEOUT = 5000

    /**
     * 健康检查结果。
     *
     * @property isAvailable 是否可用
     * @property latency 延迟（毫秒）
     * @property errorMessage 错误信息
     */
    data class HealthResult(
        val isAvailable: Boolean,
        val latency: Long = 0,
        val errorMessage: String? = null
    )

    /**
     * 检查主机连通性。
     *
     * @param host 主机地址
     * @param port 端口
     * @param timeout 超时时间（毫秒）
     * @return 健康检查结果
     */
    suspend fun checkHost(
        host: String,
        port: Int = 443,
        timeout: Int = DEFAULT_TIMEOUT
    ): HealthResult = withContext(Dispatchers.IO) {
        val startTime = System.currentTimeMillis()
        return@withContext try {
            val socket = Socket()
            val address = InetSocketAddress(host, port)
            socket.connect(address, timeout)
            socket.close()
            val latency = System.currentTimeMillis() - startTime
            Log.d(TAG, "Host $host:$port reachable, latency: ${latency}ms")
            HealthResult(true, latency)
        } catch (e: Exception) {
            Log.e(TAG, "Host $host:$port unreachable", e)
            HealthResult(false, errorMessage = e.message)
        }
    }

    /**
     * Ping 服务器（ICMP）。
     *
     * @param host 主机地址
     * @param timeout 超时时间
     * @return 健康检查结果
     */
    suspend fun ping(
        host: String,
        timeout: Int = DEFAULT_TIMEOUT
    ): HealthResult = withContext(Dispatchers.IO) {
        return@withContext try {
            val process = Runtime.getRuntime().exec("ping -c 1 -W ${timeout / 1000} $host")
            val exitCode = process.waitFor()
            val latency = parsePingLatency(process.inputStream.bufferedReader().readText())
            HealthResult(exitCode == 0, latency)
        } catch (e: Exception) {
            HealthResult(false, errorMessage = e.message)
        }
    }

    /**
     * 解析 Ping 延迟。
     */
    private fun parsePingLatency(output: String): Long {
        val regex = Regex("time=([\\d.]+)")
        val match = regex.find(output) ?: return 0L
        return match.groupValues[1].toFloatOrNull()?.toLong() ?: 0L
    }

    /**
     * 批量检查多个主机。
     *
     * @param hosts 主机列表
     * @param port 端口
     * @return 主机到健康结果的映射
     */
    suspend fun checkMultipleHosts(
        hosts: List<String>,
        port: Int = 443
    ): Map<String, HealthResult> {
        val results = mutableMapOf<String, HealthResult>()
        hosts.forEach { host ->
            results[host] = checkHost(host, port)
        }
        return results
    }

    /**
     * 获取最快的主机。
     *
     * @param hosts 主机列表
     * @param port 端口
     * @return 最快的主机地址
     */
    suspend fun getFastestHost(
        hosts: List<String>,
        port: Int = 443
    ): String? {
        val results = checkMultipleHosts(hosts, port)
        return results.entries
            .filter { it.value.isAvailable }
            .minByOrNull { it.value.latency }
            ?.key
    }
}
