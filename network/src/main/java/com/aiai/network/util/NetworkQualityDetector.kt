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

/**
 * 网络连接质量检测器。
 *
 * 实时检测网络连接质量，包括延迟、丢包率、带宽等。
 */
class NetworkQualityDetector {

    companion object {
        private const val TAG = "NetworkQualityDetector"
        private const val TEST_URL = "https://www.google.com/generate_204"
    }

    private val qualityCache = ConcurrentHashMap<String, QualitySample>()
    private val scheduler: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor { r ->
        Thread(r, "quality-detector").apply { isDaemon = true }
    }

    @Volatile
    var currentQuality: NetworkQuality = NetworkQuality.UNKNOWN
        private set

    /**
     * 开始定期检测。
     *
     * @param intervalMs 检测间隔
     */
    fun start(intervalMs: Long = 30_000) {
        scheduler.scheduleAtFixedRate({
            try {
                detectQuality()
            } catch (e: Exception) {
                Log.w(TAG, "Detection failed: ${e.message}")
            }
        }, 0, intervalMs, TimeUnit.MILLISECONDS)
    }

    /**
     * 停止检测。
     */
    fun stop() {
        scheduler.shutdown()
    }

    /**
     * 检测网络质量。
     */
    private fun detectQuality() {
        val startTime = System.nanoTime()
        try {
            val connection = java.net.URL(TEST_URL).openConnection() as java.net.HttpURLConnection
            connection.connectTimeout = 3000
            connection.readTimeout = 3000
            connection.requestMethod = "HEAD"
            connection.connect()
            val responseCode = connection.responseCode
            connection.disconnect()

            val latencyMs = (System.nanoTime() - startTime) / 1_000_000

            currentQuality = when {
                responseCode != 204 -> NetworkQuality.POOR
                latencyMs < 100 -> NetworkQuality.EXCELLENT
                latencyMs < 300 -> NetworkQuality.GOOD
                latencyMs < 1000 -> NetworkQuality.FAIR
                else -> NetworkQuality.POOR
            }

            Log.d(TAG, "Network quality: $currentQuality (${latencyMs}ms)")
        } catch (e: Exception) {
            currentQuality = NetworkQuality.OFFLINE
            Log.w(TAG, "Network offline: ${e.message}")
        }
    }

    /**
     * 获取质量历史样本。
     */
    fun getQualityHistory(): List<QualitySample> {
        return qualityCache.values.sortedBy { it.timestamp }
    }

    /**
     * 网络质量等级。
     */
    enum class NetworkQuality {
        UNKNOWN,
        EXCELLENT,
        GOOD,
        FAIR,
        POOR,
        OFFLINE;

        /**
         * 是否适合视频通话。
         */
        fun supportsVideoCall(): Boolean {
            return this == EXCELLENT || this == GOOD
        }

        /**
         * 是否适合语音通话。
         */
        fun supportsVoiceCall(): Boolean {
            return this != POOR && this != OFFLINE
        }
    }

    /**
     * 质量样本。
     */
    data class QualitySample(
        val quality: NetworkQuality,
        val latencyMs: Long,
        val timestamp: Long,
    )
}
