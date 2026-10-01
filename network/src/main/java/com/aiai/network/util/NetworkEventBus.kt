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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 网络状态事件总线。
 *
 * 全局网络状态事件分发。
 */
object NetworkEventBus {

    private const val TAG = "NetworkEventBus"

    private val _events = MutableStateFlow<NetworkEvent>(NetworkEvent.Idle)
    val events: StateFlow<NetworkEvent> = _events.asStateFlow()

    private val listeners = mutableListOf<(NetworkEvent) -> Unit>()

    /**
     * 发送网络事件。
     */
    fun emit(event: NetworkEvent) {
        _events.value = event
        listeners.forEach { it(event) }
        Log.d(TAG, "Event: $event")
    }

    /**
     * 添加事件监听器。
     */
    fun addListener(listener: (NetworkEvent) -> Unit) {
        listeners.add(listener)
    }

    /**
     * 移除事件监听器。
     */
    fun removeListener(listener: (NetworkEvent) -> Unit) {
        listeners.remove(listener)
    }

    /**
     * 清除所有监听器。
     */
    fun clearListeners() {
        listeners.clear()
    }
}

/**
 * 网络事件。
 */
sealed class NetworkEvent {
    /** 空闲 */
    object Idle : NetworkEvent()

    /** 请求开始 */
    data class RequestStart(val url: String, val method: String) : NetworkEvent()

    /** 请求成功 */
    data class RequestSuccess(val url: String, val code: Int, val durationMs: Long) : NetworkEvent()

    /** 请求失败 */
    data class RequestFailed(val url: String, val error: String) : NetworkEvent()

    /** 网络状态变化 */
    data class NetworkChanged(val isAvailable: Boolean, val type: String) : NetworkEvent()

    /** 重连开始 */
    data class Reconnecting(val attempt: Int, val delayMs: Long) : NetworkEvent()

    /** 缓存命中 */
    data class CacheHit(val url: String) : NetworkEvent()

    /** 缓存未命中 */
    data class CacheMiss(val url: String) : NetworkEvent()
}
