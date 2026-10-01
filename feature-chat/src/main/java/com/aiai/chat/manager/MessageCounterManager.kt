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
 * 消息计数器管理器
 *
 * 统计每条会话的消息数量和字数。
 */
class MessageCounterManager(private val context: Context) {

    data class ConversationCount(
        val conversationId: String,
        val messageCount: Int,
        val totalChars: Int,
        val userMessages: Int,
        val aiMessages: Int
    )

    private val _counts = MutableStateFlow<Map<String, ConversationCount>>(emptyMap())
    val counts: StateFlow<Map<String, ConversationCount>> = _counts.asStateFlow()

    fun incrementMessage(conversationId: String, isUser: Boolean, charCount: Int) {
        val current = _counts.value[conversationId] ?: ConversationCount(conversationId, 0, 0, 0, 0)
        _counts.value = _counts.value + (conversationId to current.copy(
            messageCount = current.messageCount + 1,
            totalChars = current.totalChars + charCount,
            userMessages = current.userMessages + if (isUser) 1 else 0,
            aiMessages = current.aiMessages + if (!isUser) 1 else 0
        ))
    }

    fun getCount(conversationId: String): ConversationCount? {
        return _counts.value[conversationId]
    }

    fun clearConversation(conversationId: String) {
        _counts.value = _counts.value - conversationId
    }

    fun getTotalMessages(): Int {
        return _counts.value.values.sumOf { it.messageCount }
    }

    fun getTotalChars(): Int {
        return _counts.value.values.sumOf { it.totalChars }
    }
}
