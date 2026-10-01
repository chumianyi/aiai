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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 网络性能监控器。
 *
 * 收集和分析网络性能指标。
 */
class NetworkPerformanceMonitor {

    companion object {
        private const val TAG = "NetworkPerfMonitor"
    }

    private val latencySamples = mutableListOf<LatencySample>()
    private val throughputSamples = mutableListOf<ThroughputSample>()
    private val dateFormat = SimpleDateFormat("HH:mm:ss.SSS", Locale.US)

    /**
     * 记录延迟样本。
     */
    fun recordLatency(url: String, latencyMs: Long, success: Boolean) {
        synchronized(latencySamples) {
            latencySamples.add(LatencySample(
                url = url,
                latencyMs = latencyMs,
                success = success,
                timestamp = System.currentTimeMillis(),
            ))
            // 保留最近1000条
            if (latencySamples.size > 1000) {
                latencySamples.removeAt(0)
            }
        }
    }

    /**
     * 记录吞吐量样本。
     */
    fun recordThroughput(bytes: Long, durationMs: Long) {
        synchronized(throughputSamples) {
            throughputSamples.add(ThroughputSample(
                bytes = bytes,
                durationMs = durationMs,
                timestamp = System.currentTimeMillis(),
            ))
            if (throughputSamples.size > 500) {
                throughputSamples.removeAt(0)
            }
        }
    }

    /**
     * 获取平均延迟。
     */
    fun getAverageLatency(): Double {
        synchronized(latencySamples) {
            if (latencySamples.isEmpty()) return 0.0
            return latencySamples.map { it.latencyMs }.average()
        }
    }

    /**
     * 获取P95延迟。
     */
    fun getP95Latency(): Double {
        synchronized(latencySamples) {
            if (latencySamples.isEmpty()) return 0.0
            val sorted = latencySamples.map { it.latencyMs }.sorted()
            val index = (sorted.size * 0.95).toInt().coerceAtMost(sorted.size - 1)
            return sorted[index].toDouble()
        }
    }

    /**
     * 获取平均吞吐量（KB/s）。
     */
    fun getAverageThroughput(): Double {
        synchronized(throughputSamples) {
            if (throughputSamples.isEmpty()) return 0.0
            return throughputSamples.map { it.bytes / (it.durationMs / 1000.0) }.average() / 1024.0
        }
    }

    /**
     * 生成性能报告。
     */
    fun generateReport(): String {
        val sb = StringBuilder()
        sb.appendLine("=== Network Performance Report ===")
        sb.appendLine("Time: ${dateFormat.format(Date())}")
        sb.appendLine("Samples: ${latencySamples.size} latency, ${throughputSamples.size} throughput")
        sb.appendLine("Avg Latency: ${"%.1f".format(getAverageLatency())}ms")
        sb.appendLine("P95 Latency: ${"%.1f".format(getP95Latency())}ms")
        sb.appendLine("Avg Throughput: ${"%.1f".format(getAverageThroughput())} KB/s")
        sb.appendLine("==================================")
        return sb.toString()
    }

    /**
     * 清除所有样本。
     */
    fun clear() {
        synchronized(latencySamples) { latencySamples.clear() }
        synchronized(throughputSamples) { throughputSamples.clear() }
    }

    /**
     * 延迟样本数据类。
     */
    private data class LatencySample(
        val url: String,
        val latencyMs: Long,
        val success: Boolean,
        val timestamp: Long,
    )

    /**
     * 吞吐量样本数据类。
     */
    private data class ThroughputSample(
        val bytes: Long,
        val durationMs: Long,
        val timestamp: Long,
    )
}
