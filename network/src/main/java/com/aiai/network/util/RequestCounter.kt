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
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

/**
 * 请求计数器。
 *
 * 统计API请求数量、成功率、平均耗时等指标。
 */
class RequestCounter {

    companion object {
        private const val TAG = "RequestCounter"
    }

    private val totalRequests = AtomicLong(0)
    private val successRequests = AtomicLong(0)
    private val failedRequests = AtomicLong(0)
    private val totalLatencyMs = AtomicLong(0)
    private val requestStats = ConcurrentHashMap<String, EndpointStats>()

    private data class EndpointStats(
        val count: AtomicLong = AtomicLong(0),
        val totalLatency: AtomicLong = AtomicLong(0),
        val errors: AtomicLong = AtomicLong(0),
    )

    /**
     * 记录一次请求。
     *
     * @param endpoint 请求端点
     * @param success 是否成功
     * @param latencyMs 耗时
     */
    fun record(endpoint: String, success: Boolean, latencyMs: Long) {
        totalRequests.incrementAndGet()
        totalLatencyMs.addAndGet(latencyMs)
        if (success) successRequests.incrementAndGet()
        else failedRequests.incrementAndGet()

        val stats = requestStats.getOrPut(endpoint) { EndpointStats() }
        stats.count.incrementAndGet()
        stats.totalLatency.addAndGet(latencyMs)
        if (!success) stats.errors.incrementAndGet()
    }

    /**
     * 获取总请求数。
     */
    fun getTotalRequests(): Long = totalRequests.get()

    /**
     * 获取成功率。
     */
    fun getSuccessRate(): Double {
        val total = totalRequests.get()
        if (total == 0L) return 0.0
        return successRequests.get() * 100.0 / total
    }

    /**
     * 获取平均延迟。
     */
    fun getAverageLatency(): Double {
        val total = totalRequests.get()
        if (total == 0L) return 0.0
        return totalLatencyMs.get() / total.toDouble()
    }

    /**
     * 获取端点统计信息。
     */
    fun getEndpointStats(): Map<String, Map<String, Long>> {
        return requestStats.mapValues { (_, stats) ->
            mapOf(
                "count" to stats.count.get(),
                "errors" to stats.errors.get(),
                "avgLatency" to if (stats.count.get() > 0) stats.totalLatency.get() / stats.count.get() else 0L,
            )
        }
    }

    /**
     * 重置所有统计。
     */
    fun reset() {
        totalRequests.set(0)
        successRequests.set(0)
        failedRequests.set(0)
        totalLatencyMs.set(0)
        requestStats.clear()
    }

    /**
     * 打印统计报告。
     */
    fun printReport() {
        Log.d(TAG, "=== Request Stats ===")
        Log.d(TAG, "Total: ${getTotalRequests()}")
        Log.d(TAG, "Success Rate: ${"%.2f".format(getSuccessRate())}%")
        Log.d(TAG, "Avg Latency: ${"%.1f".format(getAverageLatency())}ms")
        Log.d(TAG, "======================")
    }
}
