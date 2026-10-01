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
 * 消息已读回执管理器
 *
 * 管理消息的已读回执功能。
 */
class ReadReceiptManager(private val context: Context) {

    data class ReceiptConfig(
        val sendReadReceipt: Boolean = true,
        val showReadReceipt: Boolean = true
    )

    private val _config = MutableStateFlow(ReceiptConfig())
    val config: StateFlow<ReceiptConfig> = _config.asStateFlow()

    private val _readByMap = MutableStateFlow<Map<String, List<String>>>(emptyMap())
    val readByMap: StateFlow<Map<String, List<String>>> = _readByMap.asStateFlow()

    fun markAsRead(messageId: String, userId: String) {
        val current = _readByMap.value[messageId] ?: emptyList()
        if (userId !in current) {
            _readByMap.value = _readByMap.value + (messageId to (current + userId))
        }
    }

    fun getReadUsers(messageId: String): List<String> {
        return _readByMap.value[messageId] ?: emptyList()
    }

    fun updateConfig(config: ReceiptConfig) {
        _config.value = config
    }

    fun shouldSendReceipt(): Boolean {
        return _config.value.sendReadReceipt
    }
}
