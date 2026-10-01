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
 * 心跳管理器，定时发送心跳消息保持连接活跃。
 *
 * @param intervalMs 心跳间隔（毫秒），默认30秒
 */
class HeartbeatManager(
    private val intervalMs: Long = 30_000L,
) {

    private val scheduler = Executors.newSingleThreadScheduledExecutor { r ->
        Thread(r, "ws-heartbeat").apply { isDaemon = true }
    }

    private var heartbeatTask: ScheduledFuture<*>? = null
    private var onHeartbeat: (() -> Unit)? = null

    /**
     * 设置心跳回调。
     *
     * @param callback 心跳时执行的回调
     */
    fun setOnHeartbeat(callback: () -> Unit) {
        onHeartbeat = callback
    }

    /**
     * 启动心跳定时器。
     */
    fun start() {
        stop()
        heartbeatTask = scheduler.scheduleAtFixedRate(
            {
                try {
                    onHeartbeat?.invoke()
                } catch (e: Exception) {
                    // 忽略心跳异常
                }
            },
            intervalMs,
            intervalMs,
            TimeUnit.MILLISECONDS,
        )
    }

    /**
     * 停止心跳定时器。
     */
    fun stop() {
        heartbeatTask?.cancel(false)
        heartbeatTask = null
    }

    /**
     * 是否已启动。
     *
     * @return true如果心跳运行中
     */
    fun isRunning(): Boolean = heartbeatTask != null && !heartbeatTask!!.isDone
}
