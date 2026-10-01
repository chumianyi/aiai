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
 * 消息高亮管理器
 *
 * 管理搜索结果中的消息高亮显示。
 */
class HighlightManager(private val context: Context) {

    private val _highlightedMessageIds = MutableStateFlow<Set<String>>(emptySet())
    val highlightedMessageIds: StateFlow<Set<String>> = _highlightedMessageIds.asStateFlow()

    private val _currentHighlightIndex = MutableStateFlow(0)
    val currentHighlightIndex: StateFlow<Int> = _currentHighlightIndex.asStateFlow()

    fun setHighlightedMessages(messageIds: List<String>) {
        _highlightedMessageIds.value = messageIds.toSet()
        _currentHighlightIndex.value = 0
    }

    fun clearHighlight() {
        _highlightedMessageIds.value = emptySet()
        _currentHighlightIndex.value = 0
    }

    fun isHighlighted(messageId: String): Boolean {
        return messageId in _highlightedMessageIds.value
    }

    fun nextHighlight() {
        val count = _highlightedMessageIds.value.size
        if (count > 0) {
            _currentHighlightIndex.value = (_currentHighlightIndex.value + 1) % count
        }
    }

    fun previousHighlight() {
        val count = _highlightedMessageIds.value.size
        if (count > 0) {
            _currentHighlightIndex.value = (_currentHighlightIndex.value - 1 + count) % count
        }
    }

    fun getHighlightCount(): Int {
        return _highlightedMessageIds.value.size
    }
}
