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
 * 会话归档管理器
 *
 * 管理会话的归档和取消归档。
 */
class ConversationArchiveManager(private val context: Context) {

    private val _archivedConversationIds = MutableStateFlow<Set<String>>(emptySet())
    val archivedConversationIds: StateFlow<Set<String>> = _archivedConversationIds.asStateFlow()

    fun archiveConversation(conversationId: String) {
        _archivedConversationIds.value = _archivedConversationIds.value + conversationId
    }

    fun unarchiveConversation(conversationId: String) {
        _archivedConversationIds.value = _archivedConversationIds.value - conversationId
    }

    fun isArchived(conversationId: String): Boolean {
        return conversationId in _archivedConversationIds.value
    }

    fun getArchivedCount(): Int {
        return _archivedConversationIds.value.size
    }

    fun clearAll() {
        _archivedConversationIds.value = emptySet()
    }
}
