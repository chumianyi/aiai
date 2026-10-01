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

import com.aiai.chat.data.model.PromptSuggestion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 提示词建议管理器
 *
 * 管理首页和发现页展示的提示词推荐内容。
 * 支持分类筛选、使用次数统计、热门推荐。
 */
class PromptSuggestionManager {

    private val _suggestions = MutableStateFlow<List<PromptSuggestion>>(emptyList())
    val suggestions: StateFlow<List<PromptSuggestion>> = _suggestions.asStateFlow()

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories.asStateFlow()

    init {
        loadDefaultSuggestions()
    }

    private fun loadDefaultSuggestions() {
        val defaults = PromptSuggestion.defaultSuggestions()
        _suggestions.value = defaults
        _categories.value = defaults.map { it.category }.distinct()
    }

    /**
     * 获取热门提示词（按使用次数排序）
     */
    fun getHotSuggestions(limit: Int = 6): List<PromptSuggestion> {
        return _suggestions.value
            .sortedByDescending { it.useCount }
            .take(limit)
    }

    /**
     * 获取最新提示词
     */
    fun getNewSuggestions(limit: Int = 4): List<PromptSuggestion> {
        return _suggestions.value
            .filter { it.isNew }
            .take(limit)
    }

    /**
     * 按分类获取提示词
     */
    fun getByCategory(category: String): List<PromptSuggestion> {
        return _suggestions.value.filter { it.category == category }
    }

    /**
     * 增加使用次数
     */
    fun incrementUseCount(id: String) {
        _suggestions.value = _suggestions.value.map {
            if (it.id == id) it.copy(useCount = it.useCount + 1) else it
        }
    }

    /**
     * 搜索提示词
     */
    fun search(query: String): List<PromptSuggestion> {
        if (query.isBlank()) return emptyList()
        return _suggestions.value.filter {
            it.title.contains(query, ignoreCase = true) ||
            it.content.contains(query, ignoreCase = true) ||
            it.tags.any { tag -> tag.contains(query, ignoreCase = true) }
        }
    }
}
