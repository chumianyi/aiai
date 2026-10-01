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
import com.aiai.chat.data.model.PromptSuggestion
import com.aiai.chat.manager.PromptSuggestionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 首页ViewModel
 *
 * 管理首页内容：推荐提示词、快捷入口、最近对话、使用统计。
 */
class HomeViewModel : ViewModel() {

    private val promptManager = PromptSuggestionManager()

    private val _hotPrompts = MutableStateFlow<List<PromptSuggestion>>(emptyList())
    val hotPrompts: StateFlow<List<PromptSuggestion>> = _hotPrompts.asStateFlow()

    private val _recentConversations = MutableStateFlow<List<Conversation>>(emptyList())
    val recentConversations: StateFlow<List<Conversation>> = _recentConversations.asStateFlow()

    private val _usageStats = MutableStateFlow(UsageStats())
    val usageStats: StateFlow<UsageStats> = _usageStats.asStateFlow()

    private val _quickActions = MutableStateFlow<List<QuickAction>>(emptyList())
    val quickActions: StateFlow<List<QuickAction>> = _quickActions.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadHomeData()
    }

    /**
     * 加载首页数据
     */
    fun loadHomeData() {
        viewModelScope.launch {
            _isLoading.value = true

            // 加载热门提示词
            _hotPrompts.value = promptManager.getHotSuggestions(6)

            // 加载最近对话
            _recentConversations.value = listOf(
                Conversation(id = "1", title = "AI编程助手", lastMessage = "Kotlin协程最佳实践", updatedAt = System.currentTimeMillis() - 3600_000),
                Conversation(id = "2", title = "写作助手", lastMessage = "帮我润色一封邮件", updatedAt = System.currentTimeMillis() - 7200_000),
                Conversation(id = "3", title = "翻译", lastMessage = "Translate this paragraph...", updatedAt = System.currentTimeMillis() - 86400_000)
            )

            // 加载快捷入口
            _quickActions.value = listOf(
                QuickAction("new_chat", "新建对话", "开始新的AI对话"),
                QuickAction("image_gen", "AI画图", "生成创意图片"),
                QuickAction("code", "写代码", "编程助手"),
                QuickAction("translate", "翻译", "多语言互译")
            )

            // 加载使用统计
            _usageStats.value = UsageStats(
                todayMessages = 12,
                totalConversations = 28,
                totalTokens = 15600,
                favoriteModel = "GPT-4o mini"
            )

            _isLoading.value = false
        }
    }

    /**
     * 刷新数据
     */
    fun refresh() {
        loadHomeData()
    }

    /**
     * 使用统计数据类
     */
    data class UsageStats(
        val todayMessages: Int = 0,
        val totalConversations: Int = 0,
        val totalTokens: Int = 0,
        val favoriteModel: String = ""
    )

    /**
     * 快捷入口数据类
     */
    data class QuickAction(
        val id: String,
        val title: String,
        val subtitle: String
    )
}
