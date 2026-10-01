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
 * 消息转发管理器
 *
 * 管理消息转发到其他会话的功能。
 */
class MessageForwardManager(private val context: Context) {

    data class ForwardTask(
        val messageIds: List<String>,
        val targetConversationId: String,
        val timestamp: Long = System.currentTimeMillis()
    )

    private val _forwardTasks = MutableStateFlow<List<ForwardTask>>(emptyList())
    val forwardTasks: StateFlow<List<ForwardTask>> = _forwardTasks.asStateFlow()

    private val _forwarding = MutableStateFlow(false)
    val forwarding: StateFlow<Boolean> = _forwarding.asStateFlow()

    suspend fun forwardMessages(messageIds: List<String>, targetConversationId: String): Boolean {
        _forwarding.value = true
        return try {
            val task = ForwardTask(messageIds, targetConversationId)
            _forwardTasks.value = _forwardTasks.value + task
            true
        } finally {
            _forwarding.value = false
        }
    }

    fun getForwardHistory(): List<ForwardTask> {
        return _forwardTasks.value
    }
}
