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

/**
 * 请求节流器。
 *
 * 用于控制相同请求的频率，避免重复请求。
 */
class RequestThrottler(
    private val defaultInterval: Long = 1000L
) {

    companion object {
        private const val TAG = "RequestThrottler"
    }

    private val lastRequestTime = ConcurrentHashMap<String, Long>()

    /**
     * 是否允许请求。
     *
     * @param key 请求唯一标识
     * @param interval 间隔时间（毫秒）
     * @return true 表示允许请求
     */
    fun shouldRequest(key: String, interval: Long = defaultInterval): Boolean {
        val now = System.currentTimeMillis()
        val lastTime = lastRequestTime[key] ?: 0L

        return if (now - lastTime >= interval) {
            lastRequestTime[key] = now
            true
        } else {
            Log.d(TAG, "Request throttled: $key")
            false
        }
    }

    /**
     * 执行请求（如果允许）。
     *
     * @param key 请求唯一标识
     * @param interval 间隔时间
     * @param block 请求执行块
     */
    suspend fun throttle(
        key: String,
        interval: Long = defaultInterval,
        block: suspend () -> Unit
    ) {
        if (shouldRequest(key, interval)) {
            block()
        }
    }

    /**
     * 清除指定 key 的记录。
     *
     * @param key 请求唯一标识
     */
    fun clear(key: String) {
        lastRequestTime.remove(key)
    }

    /**
     * 清除所有记录。
     */
    fun clearAll() {
        lastRequestTime.clear()
    }

    /**
     * 获取距下次请求的剩余时间。
     *
     * @param key 请求唯一标识
     * @param interval 间隔时间
     * @return 剩余时间（毫秒）
     */
    fun remainingTime(key: String, interval: Long = defaultInterval): Long {
        val now = System.currentTimeMillis()
        val lastTime = lastRequestTime[key] ?: 0L
        val elapsed = now - lastTime
        return (interval - elapsed).coerceAtLeast(0)
    }
}
