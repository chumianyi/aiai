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
 * 会话静音管理器
 *
 * 管理会话的消息通知静音。
 */
class ConversationMuteManager(private val context: Context) {

    data class MuteConfig(
        val muted: Boolean = false,
        val muteUntil: Long = 0L
    )

    private val _mutedConversations = MutableStateFlow<Map<String, MuteConfig>>(emptyMap())
    val mutedConversations: StateFlow<Map<String, MuteConfig>> = _mutedConversations.asStateFlow()

    fun muteConversation(conversationId: String, durationHours: Long = 0) {
        val until = if (durationHours > 0) {
            System.currentTimeMillis() + durationHours * 60 * 60 * 1000
        } else {
            0L // 永久静音
        }
        _mutedConversations.value = _mutedConversations.value +
                (conversationId to MuteConfig(muted = true, muteUntil = until))
    }

    fun unmuteConversation(conversationId: String) {
        _mutedConversations.value = _mutedConversations.value - conversationId
    }

    fun isMuted(conversationId: String): Boolean {
        val config = _mutedConversations.value[conversationId] ?: return false
        if (!config.muted) return false
        if (config.muteUntil == 0L) return true
        return System.currentTimeMillis() < config.muteUntil
    }

    fun getMutedCount(): Int {
        return _mutedConversations.value.size
    }
}
