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
import com.aiai.chat.data.model.Conversation
import com.aiai.chat.sorter.ConversationSorter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 会话列表ViewModel
 *
 * 管理会话列表的展示、搜索、编辑模式、删除等操作。
 */
class ConversationListViewModel : ViewModel() {

    private val _conversations = MutableStateFlow<List<Conversation>>(emptyList())
    val conversations: StateFlow<List<Conversation>> = _conversations.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isEditMode = MutableStateFlow(false)
    val isEditMode: StateFlow<Boolean> = _isEditMode.asStateFlow()

    private val _selectedIds = MutableStateFlow<Set<String>>(emptySet())
    val selectedIds: StateFlow<Set<String>> = _selectedIds.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadConversations()
    }

    /**
     * 加载会话列表
     */
    fun loadConversations() {
        viewModelScope.launch {
            _isLoading.value = true
            // 模拟数据加载
            val mockList = listOf(
                Conversation(id = "1", title = "AI编程助手", lastMessage = "帮我写一个Kotlin协程的例子", updatedAt = System.currentTimeMillis() - 3600_000, isPinned = true),
                Conversation(id = "2", title = "旅行攻略", lastMessage = "日本京都5天行程规划", updatedAt = System.currentTimeMillis() - 86400_000),
                Conversation(id = "3", title = "读书笔记", lastMessage = "《人类简史》核心观点总结", updatedAt = System.currentTimeMillis() - 2 * 86400_000),
                Conversation(id = "4", title = "健身计划", lastMessage = "居家无器械训练方案", updatedAt = System.currentTimeMillis() - 3 * 86400_000, isFavorite = true),
                Conversation(id = "5", title = "产品需求文档", lastMessage = "聊天App的PRD初稿", updatedAt = System.currentTimeMillis() - 5 * 86400_000)
            )
            _conversations.value = ConversationSorter.sort(mockList)
            _isLoading.value = false
        }
    }

    /**
     * 搜索
     */
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        // 过滤逻辑在UI层或这里处理
    }

    /**
     * 进入编辑模式
     */
    fun enterEditMode() {
        _isEditMode.value = true
        _selectedIds.value = emptySet()
    }

    /**
     * 退出编辑模式
     */
    fun exitEditMode() {
        _isEditMode.value = false
        _selectedIds.value = emptySet()
    }

    /**
     * 切换选中状态
     */
    fun toggleSelect(conversationId: String) {
        val current = _selectedIds.value.toMutableSet()
        if (conversationId in current) {
            current.remove(conversationId)
        } else {
            current.add(conversationId)
        }
        _selectedIds.value = current
    }

    /**
     * 全选/取消全选
     */
    fun selectAll(selectAll: Boolean) {
        _selectedIds.value = if (selectAll) {
            _conversations.value.map { it.id }.toSet()
        } else {
            emptySet()
        }
    }

    /**
     * 删除选中的会话
     */
    fun deleteSelected() {
        viewModelScope.launch {
            val idsToDelete = _selectedIds.value
            _conversations.value = _conversations.value.filterNot { it.id in idsToDelete }
            exitEditMode()
        }
    }

    /**
     * 置顶/取消置顶
     */
    fun togglePin(conversationId: String) {
        _conversations.value = _conversations.value.map { conv ->
            if (conv.id == conversationId) conv.copy(isPinned = !conv.isPinned) else conv
        }.let { ConversationSorter.sort(it) }
    }
}
