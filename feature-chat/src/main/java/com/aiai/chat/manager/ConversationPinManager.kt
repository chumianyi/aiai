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
 * 会话置顶管理器
 *
 * 管理会话列表中的会话置顶。
 */
class ConversationPinManager(private val context: Context) {

    private val _pinnedConversationIds = MutableStateFlow<Set<String>>(emptySet())
    val pinnedConversationIds: StateFlow<Set<String>> = _pinnedConversationIds.asStateFlow()

    fun pinConversation(conversationId: String) {
        _pinnedConversationIds.value = _pinnedConversationIds.value + conversationId
    }

    fun unpinConversation(conversationId: String) {
        _pinnedConversationIds.value = _pinnedConversationIds.value - conversationId
    }

    fun isPinned(conversationId: String): Boolean {
        return conversationId in _pinnedConversationIds.value
    }

    fun getPinnedCount(): Int {
        return _pinnedConversationIds.value.size
    }

    fun togglePin(conversationId: String): Boolean {
        return if (isPinned(conversationId)) {
            unpinConversation(conversationId)
            false
        } else {
            pinConversation(conversationId)
            true
        }
    }
}
