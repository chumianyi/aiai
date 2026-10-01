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
 * 网络连接池。
 *
 * 管理HTTP连接池，优化连接复用。
 */
class ConnectionPool(
    private val maxIdleConnections: Int = 5,
    private val keepAliveDurationMs: Long = 5 * 60 * 1000,
) {

    companion object {
        private const val TAG = "ConnectionPool"
    }

    private val connections = ConcurrentHashMap<String, PooledConnection>()
    private val cleanupExecutor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor { r ->
        Thread(r, "conn-pool-cleanup").apply { isDaemon = true }
    }

    init {
        cleanupExecutor.scheduleAtFixedRate({
            try {
                evictIdleConnections()
            } catch (e: Exception) {
                Log.w(TAG, "Cleanup failed: ${e.message}")
            }
        }, 1, 1, TimeUnit.MINUTES)
    }

    /**
     * 获取连接。
     *
     * @param key 连接键（host:port）
     * @return 连接
     */
    fun acquire(key: String): PooledConnection {
        val existing = connections[key]
        if (existing != null && !existing.isExpired()) {
            existing.lastUsed = System.currentTimeMillis()
            return existing
        }
        val conn = PooledConnection(key)
        connections[key] = conn
        return conn
    }

    /**
     * 释放连接。
     */
    fun release(key: String) {
        connections[key]?.lastUsed = System.currentTimeMillis()
    }

    /**
     * 驱逐空闲连接。
     */
    private fun evictIdleConnections() {
        val now = System.currentTimeMillis()
        val iterator = connections.entries.iterator()
        var evicted = 0
        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (now - entry.value.lastUsed > keepAliveDurationMs) {
                iterator.remove()
                evicted++
            }
        }
        if (evicted > 0) {
            Log.d(TAG, "Evicted $evicted idle connections")
        }
    }

    /**
     * 获取当前连接数。
     */
    fun size(): Int = connections.size

    /**
     * 关闭连接池。
     */
    fun shutdown() {
        cleanupExecutor.shutdown()
        connections.clear()
    }
}

/**
 * 池化连接。
 */
data class PooledConnection(
    val key: String,
    var lastUsed: Long = System.currentTimeMillis(),
) {
    fun isExpired(): Boolean {
        return System.currentTimeMillis() - lastUsed > 5 * 60 * 1000
    }
}
