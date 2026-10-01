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
 * 消息撤销管理器
 *
 * 管理消息的撤销功能，支持撤销最近发送的消息。
 */
class UndoManager(private val context: Context) {

    data class UndoAction(
        val message: ChatMessage,
        val action: ActionType,
        val timestamp: Long = System.currentTimeMillis()
    )

    enum class ActionType {
        DELETE,
        EDIT,
        FORWARD,
        COPY
    }

    private val _undoStack = MutableStateFlow<List<UndoAction>>(emptyList())
    val undoStack: StateFlow<List<UndoAction>> = _undoStack.asStateFlow()

    private val maxUndoSize = 20

    fun pushUndo(action: UndoAction) {
        val current = _undoStack.value + action
        _undoStack.value = current.takeLast(maxUndoSize)
    }

    fun popUndo(): UndoAction? {
        val current = _undoStack.value
        if (current.isEmpty()) return null
        val last = current.last()
        _undoStack.value = current.dropLast(1)
        return last
    }

    fun canUndo(): Boolean {
        return _undoStack.value.isNotEmpty()
    }

    fun clearUndoStack() {
        _undoStack.value = emptyList()
    }

    fun getUndoCount(): Int {
        return _undoStack.value.size
    }
}
