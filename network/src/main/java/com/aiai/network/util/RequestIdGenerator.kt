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

/**
 * 请求ID生成器。
 *
 * 生成唯一的请求ID，用于追踪和调试。
 */
class RequestIdGenerator {

    companion object {
        private const val TAG = "RequestIdGenerator"
        private const val PREFIX = "aiai"
    }

    private val counters = ConcurrentHashMap<String, Int>()

    /**
     * 生成请求ID。
     *
     * @param category 请求分类（chat/file/auth等）
     * @return 唯一请求ID
     */
    fun nextId(category: String = "default"): String {
        val count = counters.merge(category, 1) { old, _ -> old + 1 } ?: 1
        val timestamp = System.currentTimeMillis()
        return "${PREFIX}_${category}_${timestamp}_${count.toString().padStart(6, '0')}"
    }

    /**
     * 重置计数器。
     */
    fun reset() {
        counters.clear()
    }

    /**
     * 获取指定分类的请求计数。
     */
    fun getCount(category: String): Int {
        return counters[category] ?: 0
    }
}
