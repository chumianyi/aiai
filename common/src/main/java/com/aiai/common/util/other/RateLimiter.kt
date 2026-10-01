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
package com.aiai.common.util.other

import android.os.SystemClock
import java.util.concurrent.ConcurrentHashMap

/**
 * 限流器。
 *
 * 在指定时间窗口内限制最大请求次数。
 */
class RateLimiter(
    private val maxRequests: Int = 10,
    private val windowMillis: Long = 1000L
) {
    private val requestTimes = ConcurrentHashMap<String, MutableList<Long>>()

    /**
     * 是否允许请求。
     *
     * @param key 限流键
     * @return true 允许，false 限流
     */
    fun tryAcquire(key: String): Boolean {
        val now = SystemClock.elapsedRealtime()
        val times = requestTimes.getOrPut(key) { mutableListOf() }
        synchronized(times) {
            times.removeAll { now - it > windowMillis }
            return if (times.size < maxRequests) {
                times.add(now)
                true
            } else false
        }
    }

    /** 重置指定 key 的计数。 */
    fun reset(key: String) {
        requestTimes.remove(key)
    }
}
