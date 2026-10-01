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
 * 消息搜索索引管理器
 *
 * 建立消息的搜索索引，提供快速全文检索。
 */
class MessageSearchIndexManager(private val context: Context) {

    data class SearchIndexEntry(
        val messageId: String,
        val conversationId: String,
        val content: String,
        val timestamp: Long
    )

    private val _index = MutableStateFlow<List<SearchIndexEntry>>(emptyList())
    val index: StateFlow<List<SearchIndexEntry>> = _index.asStateFlow()

    fun addToIndex(message: ChatMessage, conversationId: String) {
        val entry = SearchIndexEntry(
            messageId = message.id,
            conversationId = conversationId,
            content = message.content,
            timestamp = message.timestamp
        )
        _index.value = _index.value + entry
    }

    fun removeFromIndex(messageId: String) {
        _index.value = _index.value.filterNot { it.messageId == messageId }
    }

    fun search(query: String): List<SearchIndexEntry> {
        if (query.isBlank()) return emptyList()
        return _index.value.filter {
            it.content.contains(query, ignoreCase = true)
        }.sortedByDescending { it.timestamp }
    }

    fun searchInConversation(conversationId: String, query: String): List<SearchIndexEntry> {
        return search(query).filter { it.conversationId == conversationId }
    }

    fun clearConversationIndex(conversationId: String) {
        _index.value = _index.value.filterNot { it.conversationId == conversationId }
    }

    fun clearAll() {
        _index.value = emptyList()
    }

    fun getIndexSize(): Int {
        return _index.value.size
    }
}
