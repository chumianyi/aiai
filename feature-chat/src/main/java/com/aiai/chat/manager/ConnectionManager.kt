/*
 * Copyright (c) 2026 爱Ai (AiAi)
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
package com.aiai.chat.manager

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 网络连接管理器
 *
 * 管理API连接状态和重连逻辑。
 */
class ConnectionManager(private val context: Context) {

    enum class ConnectionState {
        DISCONNECTED,
        CONNECTING,
        CONNECTED,
        RECONNECTING,
        ERROR
    }

    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

    private val _retryCount = MutableStateFlow(0)
    val retryCount: StateFlow<Int> = _retryCount.asStateFlow()

    private val _latency = MutableStateFlow(0L)
    val latency: StateFlow<Long> = _latency.asStateFlow()

    fun onConnecting() {
        _connectionState.value = ConnectionState.CONNECTING
    }

    fun onConnected() {
        _connectionState.value = ConnectionState.CONNECTED
        _retryCount.value = 0
    }

    fun onDisconnected() {
        _connectionState.value = ConnectionState.DISCONNECTED
    }

    fun onReconnecting() {
        _connectionState.value = ConnectionState.RECONNECTING
        _retryCount.value = _retryCount.value + 1
    }

    fun onError() {
        _connectionState.value = ConnectionState.ERROR
    }

    fun updateLatency(ms: Long) {
        _latency.value = ms
    }

    fun isConnected(): Boolean {
        return _connectionState.value == ConnectionState.CONNECTED
    }
}
