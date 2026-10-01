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
 * 消息已读状态管理器
 *
 * 管理消息的已读/未读状态显示。
 */
class UnreadBadgeManager(private val context: Context) {

    data class BadgeConfig(
        val showUnreadBadge: Boolean = true,
        val badgeMaxCount: Int = 99
    )

    private val _config = MutableStateFlow(BadgeConfig())
    val config: StateFlow<BadgeConfig> = _config.asStateFlow()

    private val _unreadCounts = MutableStateFlow<Map<String, Int>>(emptyMap())
    val unreadCounts: StateFlow<Map<String, Int>> = _unreadCounts.asStateFlow()

    fun incrementUnread(conversationId: String) {
        val current = _unreadCounts.value[conversationId] ?: 0
        _unreadCounts.value = _unreadCounts.value + (conversationId to (current + 1))
    }

    fun clearUnread(conversationId: String) {
        _unreadCounts.value = _unreadCounts.value + (conversationId to 0)
    }

    fun getUnreadCount(conversationId: String): Int {
        return _unreadCounts.value[conversationId] ?: 0
    }

    fun getTotalUnreadCount(): Int {
        return _unreadCounts.value.values.sum()
    }

    fun formatBadgeCount(count: Int): String {
        return if (count > _config.value.badgeMaxCount) {
            "${_config.value.badgeMaxCount}+"
        } else {
            count.toString()
        }
    }
}
