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
import com.aiai.chat.data.model.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 消息重试管理器
 *
 * 管理失败消息的自动重试策略。
 */
class RetryManager(private val context: Context) {

    data class RetryConfig(
        val maxRetries: Int = 3,
        val baseDelayMs: Long = 1000,
        val backoffMultiplier: Double = 2.0
    )

    private val _config = MutableStateFlow(RetryConfig())
    val config: StateFlow<RetryConfig> = _config.asStateFlow()

    private val _pendingRetries = MutableStateFlow<Map<String, Int>>(emptyMap())
    val pendingRetries: StateFlow<Map<String, Int>> = _pendingRetries.asStateFlow()

    /**
     * 计算重试延迟（指数退避）
     */
    fun getRetryDelay(attempt: Int): Long {
        val cfg = _config.value
        return (cfg.baseDelayMs * Math.pow(cfg.backoffMultiplier, attempt.toDouble())).toLong()
    }

    /**
     * 是否还可以重试
     */
    fun canRetry(messageId: String): Boolean {
        val attempt = _pendingRetries.value[messageId] ?: 0
        return attempt < _config.value.maxRetries
    }

    /**
     * 记录一次重试
     */
    fun recordRetry(messageId: String) {
        val current = _pendingRetries.value[messageId] ?: 0
        _pendingRetries.value = _pendingRetries.value + (messageId to (current + 1))
    }

    /**
     * 清除重试记录
     */
    fun clearRetry(messageId: String) {
        _pendingRetries.value = _pendingRetries.value - messageId
    }

    fun updateConfig(config: RetryConfig) {
        _config.value = config
    }
}
