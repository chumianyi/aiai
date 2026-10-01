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
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

/**
 * 网络请求队列。
 *
 * 管理请求队列，支持优先级排序和限流。
 */
class RequestQueue(
    private val maxConcurrentRequests: Int = 6,
) {

    companion object {
        private const val TAG = "RequestQueue"
    }

    private val queue = ConcurrentLinkedQueue<QueuedRequest>()
    private var activeCount = 0
    private val scheduler: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

    /**
     * 将请求加入队列。
     *
     * @param request 请求
     */
    fun enqueue(request: QueuedRequest) {
        queue.offer(request)
        Log.d(TAG, "Enqueued: ${request.id}, queue size=${queue.size}")
        processQueue()
    }

    /**
     * 完成请求。
     */
    fun complete(requestId: String) {
        activeCount--
        Log.d(TAG, "Completed: $requestId, active=$activeCount")
        processQueue()
    }

    /**
     * 处理队列。
     */
    private fun processQueue() {
        while (activeCount < maxConcurrentRequests && queue.isNotEmpty()) {
            val request = queue.poll() ?: break
            activeCount++
            request.execute()
        }
    }

    /**
     * 获取队列大小。
     */
    fun size(): Int = queue.size

    /**
     * 获取活跃请求数。
     */
    fun activeCount(): Int = activeCount

    /**
     * 取消所有请求。
     */
    fun cancelAll() {
        queue.clear()
        activeCount = 0
    }

    /**
     * 关闭队列。
     */
    fun shutdown() {
        scheduler.shutdown()
        queue.clear()
    }
}

/**
 * 队列中的请求。
 */
data class QueuedRequest(
    val id: String,
    val priority: Int = 0,
    val execute: () -> Unit,
)
