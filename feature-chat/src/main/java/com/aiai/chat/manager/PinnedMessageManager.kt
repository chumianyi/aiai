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
 * 消息置顶管理器
 *
 * 管理会话中重要消息的置顶功能。
 */
class PinnedMessageManager(private val context: Context) {

    private val _pinnedMessages = MutableStateFlow<Map<String, List<String>>>(emptyMap())
    val pinnedMessages: StateFlow<Map<String, List<String>>> = _pinnedMessages.asStateFlow()

    fun pinMessage(conversationId: String, messageId: String) {
        val current = _pinnedMessages.value[conversationId] ?: emptyList()
        if (messageId !in current) {
            _pinnedMessages.value = _pinnedMessages.value + (conversationId to (current + messageId))
        }
    }

    fun unpinMessage(conversationId: String, messageId: String) {
        val current = _pinnedMessages.value[conversationId] ?: return
        _pinnedMessages.value = _pinnedMessages.value + (conversationId to current - messageId)
    }

    fun isPinned(conversationId: String, messageId: String): Boolean {
        return _pinnedMessages.value[conversationId]?.contains(messageId) == true
    }

    fun getPinnedMessageIds(conversationId: String): List<String> {
        return _pinnedMessages.value[conversationId] ?: emptyList()
    }

    fun hasPinnedMessages(conversationId: String): Boolean {
        return getPinnedMessageIds(conversationId).isNotEmpty()
    }

    fun clearPinnedMessages(conversationId: String) {
        _pinnedMessages.value = _pinnedMessages.value - conversationId
    }
}
