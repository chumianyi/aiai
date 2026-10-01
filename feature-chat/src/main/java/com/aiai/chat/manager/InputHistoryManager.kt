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
 * 输入历史管理器
 *
 * 管理用户输入框的历史记录，支持快速选择和清空。
 */
class InputHistoryManager(private val context: Context) {

    private val _history = MutableStateFlow<List<String>>(emptyList())
    val history: StateFlow<List<String>> = _history.asStateFlow()

    private val maxHistorySize = 50

    fun addToHistory(input: String) {
        if (input.isBlank()) return
        val current = _history.value.toMutableList()
        current.remove(input)
        current.add(0, input)
        _history.value = current.take(maxHistorySize)
    }

    fun clearHistory() {
        _history.value = emptyList()
    }

    fun searchHistory(query: String): List<String> {
        if (query.isBlank()) return emptyList()
        return _history.value.filter { it.contains(query, ignoreCase = true) }
    }

    fun removeFromHistory(input: String) {
        _history.value = _history.value - input
    }
}
