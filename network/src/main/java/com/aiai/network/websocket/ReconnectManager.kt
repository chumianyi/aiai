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
package com.aiai.network.websocket

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

/**
 * 重连管理器，使用指数退避策略管理WebSocket重连。
 *
 * 退避策略：
 * - 第1次：1秒
 * - 第2次：2秒
 * - 第3次：4秒
 * - 第4次：8秒
 * - 最大：30秒
 * - 超过最大重连次数后停止
 *
 * @property maxAttempts 最大重连次数
 * @property baseDelayMs 基础延迟（毫秒）
 * @property maxDelayMs 最大延迟（毫秒）
 */
class ReconnectManager(
    private val maxAttempts: Int = 10,
    private val baseDelayMs: Long = 1_000L,
    private val maxDelayMs: Long = 30_000L,
) {

    private val scheduler = Executors.newSingleThreadScheduledExecutor { r ->
        Thread(r, "ws-reconnect").apply { isDaemon = true }
    }

    private var reconnectTask: ScheduledFuture<*>? = null

    /**
     * 计算指数退避延迟时间。
     *
     * @param attempt 当前重试次数（从1开始）
     * @return 延迟毫秒数
     */
    fun calculateDelay(attempt: Int): Long {
        if (attempt > maxAttempts) return -1L
        val delay = baseDelayMs * (1L shl (attempt - 1))
        return delay.coerceAtMost(maxDelayMs)
    }

    /**
     * 调度重连任务。
     *
     * @param delayMs 延迟毫秒数
     * @param action 重连动作
     */
    fun scheduleReconnect(delayMs: Long, action: () -> Unit) {
        cancel()
        reconnectTask = scheduler.schedule({
            try {
                action()
            } catch (e: Exception) {
                // 忽略重连异常
            }
        }, delayMs, TimeUnit.MILLISECONDS)
    }

    /**
     * 取消重连任务。
     */
    fun cancel() {
        reconnectTask?.cancel(false)
        reconnectTask = null
    }

    /**
     * 是否已调度重连。
     *
     * @return true如果有重连任务在等待
     */
    fun isScheduled(): Boolean = reconnectTask != null && !reconnectTask!!.isDone
}
