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
 * 消息表情回复管理器
 *
 * 管理消息上的表情回应（Reaction）。
 */
class MessageReactionManager(private val context: Context) {

    data class Reaction(
        val emoji: String,
        val count: Int,
        val userReacted: Boolean = false
    )

    private val _reactions = MutableStateFlow<Map<String, List<Reaction>>>(emptyMap())
    val reactions: StateFlow<Map<String, List<Reaction>>> = _reactions.asStateFlow()

    fun addReaction(messageId: String, emoji: String) {
        val current = _reactions.value[messageId] ?: emptyList()
        val existing = current.find { it.emoji == emoji }
        val updated = if (existing != null) {
            current.map { if (it.emoji == emoji) it.copy(count = it.count + 1, userReacted = true) else it }
        } else {
            current + Reaction(emoji, 1, true)
        }
        _reactions.value = _reactions.value + (messageId to updated)
    }

    fun removeReaction(messageId: String, emoji: String) {
        val current = _reactions.value[messageId] ?: return
        val existing = current.find { it.emoji == emoji }
        val updated = if (existing != null && existing.count > 1) {
            current.map { if (it.emoji == emoji) it.copy(count = it.count - 1, userReacted = false) else it }
        } else {
            current.filterNot { it.emoji == emoji }
        }
        _reactions.value = _reactions.value + (messageId to updated)
    }

    fun getReactions(messageId: String): List<Reaction> {
        return _reactions.value[messageId] ?: emptyList()
    }

    fun hasReacted(messageId: String, emoji: String): Boolean {
        return _reactions.value[messageId]?.find { it.emoji == emoji }?.userReacted == true
    }
}
