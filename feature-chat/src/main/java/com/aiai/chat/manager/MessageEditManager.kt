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
 * 消息编辑管理器
 *
 * 管理消息的编辑和修改历史。
 */
class MessageEditManager(private val context: Context) {

    data class EditHistory(
        val messageId: String,
        val originalContent: String,
        val editedContent: String,
        val editTime: Long = System.currentTimeMillis()
    )

    private val _editHistory = MutableStateFlow<List<EditHistory>>(emptyList())
    val editHistory: StateFlow<List<EditHistory>> = _editHistory.asStateFlow()

    fun recordEdit(messageId: String, original: String, edited: String) {
        val history = EditHistory(messageId, original, edited)
        _editHistory.value = _editHistory.value + history
    }

    fun getEditsForMessage(messageId: String): List<EditHistory> {
        return _editHistory.value.filter { it.messageId == messageId }
    }

    fun hasEdits(messageId: String): Boolean {
        return _editHistory.value.any { it.messageId == messageId }
    }

    fun clearHistory() {
        _editHistory.value = emptyList()
    }
}
