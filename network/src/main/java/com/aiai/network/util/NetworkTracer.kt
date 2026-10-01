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
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

/**
 * 网络追踪器。
 *
 * 提供请求耗时、成功率、错误统计等网络监控功能。
 */
class NetworkTracer {

    companion object {
        private const val TAG = "NetworkTracer"
    }

    /**
     * 请求记录。
     *
     * @property url 请求URL
     * @property method 请求方法
     * @property startTime 开始时间
     * @property endTime 结束时间
     * @property statusCode 状态码
     * @property error 错误信息
     */
    data class RequestRecord(
        val url: String,
        val method: String,
        val startTime: Long,
        val endTime: Long,
        val statusCode: Int = -1,
        val error: String? = null
    ) {
        /** 请求耗时（毫秒）。 */
        val duration: Long get() = endTime - startTime
    }

    private val records = mutableListOf<RequestRecord>()
    private val totalRequests = AtomicLong(0)
    private val successRequests = AtomicLong(0)
    private val failedRequests = AtomicLong(0)
    private val totalLatency = AtomicLong(0)
    private val errorCounts = ConcurrentHashMap<String, AtomicLong>()

    /**
     * 开始追踪请求。
     *
     * @param url 请求URL
     * @param method 请求方法
     * @return 请求ID（用于结束追踪）
     */
    fun startRequest(url: String, method: String = "GET"): String {
        val requestId = "${System.nanoTime()}_${url.hashCode()}"
        Log.d(TAG, "Request started: $method $url")
        return requestId
    }

    /**
     * 结束追踪请求。
     *
     * @param requestId 请求ID
     * @param statusCode 状态码
     * @param error 错误信息
     */
    fun endRequest(
        requestId: String,
        statusCode: Int = 200,
        error: String? = null
    ) {
        totalRequests.incrementAndGet()

        if (error == null && statusCode in 200..299) {
            successRequests.incrementAndGet()
        } else {
            failedRequests.incrementAndGet()
            val errorKey = error ?: "HTTP_$statusCode"
            errorCounts.getOrPut(errorKey) { AtomicLong(0) }.incrementAndGet()
        }

        Log.d(TAG, "Request ended: $requestId, status: $statusCode")
    }

    /**
     * 记录请求完成。
     *
     * @param url 请求URL
     * @param method 请求方法
     * @param duration 耗时（毫秒）
     * @param statusCode 状态码
     * @param error 错误信息
     */
    fun recordRequest(
        url: String,
        method: String,
        duration: Long,
        statusCode: Int = 200,
        error: String? = null
    ) {
        totalRequests.incrementAndGet()
        totalLatency.addAndGet(duration)

        if (error == null && statusCode in 200..299) {
            successRequests.incrementAndGet()
        } else {
            failedRequests.incrementAndGet()
            val errorKey = error ?: "HTTP_$statusCode"
            errorCounts.getOrPut(errorKey) { AtomicLong(0) }.incrementAndGet()
        }

        synchronized(records) {
            records.add(
                RequestRecord(
                    url = url,
                    method = method,
                    startTime = System.currentTimeMillis() - duration,
                    endTime = System.currentTimeMillis(),
                    statusCode = statusCode,
                    error = error
                )
            )

            // 限制记录数量
            if (records.size > 1000) {
                records.subList(0, records.size - 1000).clear()
            }
        }
    }

    /**
     * 获取成功率。
     *
     * @return 成功率（0-100）
     */
    fun successRate(): Float {
        val total = totalRequests.get()
        return if (total == 0) 0f else successRequests.get().toFloat() / total * 100
    }

    /**
     * 获取平均延迟。
     *
     * @return 平均延迟（毫秒）
     */
    fun averageLatency(): Long {
        val total = totalRequests.get()
        return if (total == 0) 0 else totalLatency.get() / total
    }

    /**
     * 获取总请求数。
     */
    fun totalRequests(): Long = totalRequests.get()

    /**
     * 获取失败请求数。
     */
    fun failedRequests(): Long = failedRequests.get()

    /**
     * 获取错误统计。
     *
     * @return 错误类型到次数的映射
     */
    fun errorStats(): Map<String, Long> {
        return errorCounts.mapValues { it.value.get() }
    }

    /**
     * 获取最近的请求记录。
     *
     * @param limit 数量限制
     * @return 请求记录列表
     */
    fun recentRecords(limit: Int = 50): List<RequestRecord> {
        return synchronized(records) {
            records.takeLast(limit)
        }
    }

    /**
     * 重置统计。
     */
    fun reset() {
        totalRequests.set(0)
        successRequests.set(0)
        failedRequests.set(0)
        totalLatency.set(0)
        errorCounts.clear()
        synchronized(records) { records.clear() }
        Log.d(TAG, "Tracer reset")
    }

    /**
     * 获取统计摘要。
     */
    fun summary(): String {
        return buildString {
            appendLine("=== Network Tracer Summary ===")
            appendLine("Total Requests: ${totalRequests()}")
            appendLine("Success Rate: ${"%.2f".format(successRate())}%")
            appendLine("Avg Latency: ${averageLatency()}ms")
            appendLine("Failed: ${failedRequests()}")
            appendLine("Top Errors:")
            errorStats().entries.sortedByDescending { it.value }.take(5).forEach { (error, count) ->
                appendLine("  $error: $count")
            }
        }
    }
}
