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
 * 搜索历史管理器
 *
 * 管理搜索关键词的历史记录，支持增删查清空。
 */
class SearchHistoryManager(private val context: Context) {

    private val prefs = context.getSharedPreferences("search_history", Context.MODE_PRIVATE)
    private val _history = MutableStateFlow<List<String>>(emptyList())
    val history: StateFlow<List<String>> = _history.asStateFlow()

    private val maxHistorySize = 20

    init {
        loadHistory()
    }

    private fun loadHistory() {
        val saved = prefs.getStringSet("history", emptySet()) ?: emptySet()
        _history.value = saved.toList()
    }

    fun addSearch(keyword: String) {
        if (keyword.isBlank()) return
        val current = _history.value.toMutableList()
        current.remove(keyword)
        current.add(0, keyword)
        val newList = current.take(maxHistorySize)
        _history.value = newList
        prefs.edit().putStringSet("history", newList.toSet()).apply()
    }

    fun removeSearch(keyword: String) {
        val newList = _history.value.filterNot { it == keyword }
        _history.value = newList
        prefs.edit().putStringSet("history", newList.toSet()).apply()
    }

    fun clearHistory() {
        _history.value = emptyList()
        prefs.edit().remove("history").apply()
    }

    fun search(keyword: String): List<String> {
        if (keyword.isBlank()) return emptyList()
        return _history.value.filter { it.contains(keyword, ignoreCase = true) }
    }
}
