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
 * 消息标记管理器
 *
 * 管理消息的已读/未读状态。
 */
class MessageReadManager(private val context: Context) {

    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount.asStateFlow()

    private val _readMessageIds = MutableStateFlow<Set<String>>(emptySet())
    val readMessageIds: StateFlow<Set<String>> = _readMessageIds.asStateFlow()

    fun markAsRead(messageId: String) {
        _readMessageIds.value = _readMessageIds.value + messageId
    }

    fun markConversationAsRead(messageIds: List<String>) {
        _readMessageIds.value = _readMessageIds.value + messageIds
    }

    fun isRead(messageId: String): Boolean {
        return messageId in _readMessageIds.value
    }

    fun setUnreadCount(count: Int) {
        _unreadCount.value = count
    }

    fun incrementUnread() {
        _unreadCount.value = _unreadCount.value + 1
    }

    fun clearUnread() {
        _unreadCount.value = 0
    }
}
