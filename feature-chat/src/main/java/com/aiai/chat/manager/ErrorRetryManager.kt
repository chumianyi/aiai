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
 * 消息错误重试管理器
 *
 * 管理消息发送失败后的重试逻辑。
 */
class ErrorRetryManager(private val context: Context) {

    data class RetryConfig(
        val maxRetries: Int = 3,
        val retryDelayMs: Long = 1000
    )

    private val _config = MutableStateFlow(RetryConfig())
    val config: StateFlow<RetryConfig> = _config.asStateFlow()

    private val _failedMessages = MutableStateFlow<Map<String, Int>>(emptyMap())
    val failedMessages: StateFlow<Map<String, Int>> = _failedMessages.asStateFlow()

    fun recordFailure(messageId: String) {
        val current = _failedMessages.value[messageId] ?: 0
        _failedMessages.value = _failedMessages.value + (messageId to (current + 1))
    }

    fun canRetry(messageId: String): Boolean {
        val retries = _failedMessages.value[messageId] ?: 0
        return retries < _config.value.maxRetries
    }

    fun clearFailure(messageId: String) {
        _failedMessages.value = _failedMessages.value - messageId
    }

    fun getRetryCount(messageId: String): Int {
        return _failedMessages.value[messageId] ?: 0
    }

    fun updateConfig(config: RetryConfig) {
        _config.value = config
    }
}
