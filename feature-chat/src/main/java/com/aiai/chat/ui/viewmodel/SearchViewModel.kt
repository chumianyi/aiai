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
package com.aiai.chat.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiai.chat.data.model.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 搜索ViewModel
 *
 * 管理对话搜索页面：搜索框、历史搜索、搜索结果、结果高亮。
 */
class SearchViewModel : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<ChatMessage>>(emptyList())
    val searchResults: StateFlow<List<ChatMessage>> = _searchResults.asStateFlow()

    private val _searchHistory = MutableStateFlow<List<String>>(emptyList())
    val searchHistory: StateFlow<List<String>> = _searchHistory.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val _hasSearched = MutableStateFlow(false)
    val hasSearched: StateFlow<Boolean> = _hasSearched.asStateFlow()

    init {
        loadSearchHistory()
    }

    /**
     * 搜索输入变化
     */
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    /**
     * 执行搜索
     */
    fun search() {
        val query = _searchQuery.value.trim()
        if (query.isEmpty()) return

        viewModelScope.launch {
            _isSearching.value = true
            _hasSearched.value = true

            // 添加到历史
            addToSearchHistory(query)

            // 模拟搜索结果
            kotlinx.coroutines.delay(300)
            _searchResults.value = listOf(
                ChatMessage(id = "1", conversationId = "conv1", role = com.aiai.chat.data.enums.MessageRole.USER,
                    content = "如何学习Kotlin协程？"),
                ChatMessage(id = "2", conversationId = "conv1", role = com.aiai.chat.data.enums.MessageRole.ASSISTANT,
                    content = "学习Kotlin协程可以从以下几个方面入手：\n1. 理解挂起函数\n2. 掌握CoroutineScope\n3. 学习Flow..."),
                ChatMessage(id = "3", conversationId = "conv2", role = com.aiai.chat.data.enums.MessageRole.USER,
                    content = "帮我写一个文件上传的代码")
            )
            _isSearching.value = false
        }
    }

    /**
     * 清空搜索
     */
    fun clearSearch() {
        _searchQuery.value = ""
        _searchResults.value = emptyList()
        _hasSearched.value = false
    }

    /**
     * 添加搜索历史
     */
    private fun addToSearchHistory(query: String) {
        val current = _searchHistory.value.toMutableList()
        current.remove(query)
        current.add(0, query)
        _searchHistory.value = current.take(10)
    }

    /**
     * 删除指定搜索历史
     */
    fun removeSearchHistory(query: String) {
        _searchHistory.value = _searchHistory.value.filterNot { it == query }
    }

    /**
     * 清空搜索历史
     */
    fun clearSearchHistory() {
        _searchHistory.value = emptyList()
    }

    /**
     * 加载搜索历史（从本地存储）
     */
    private fun loadSearchHistory() {
        // 实际项目中从MMKV/DataStore读取
        _searchHistory.value = listOf("Kotlin协程", "AI绘画", "React Native")
    }
}
