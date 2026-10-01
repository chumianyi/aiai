/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
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
package com.aiai.core.manager

import android.util.Log

/**
 * 搜索管理器。
 *
 * 提供搜索历史、搜索建议、搜索过滤等功能。
 */
object SearchManager {

    private const val TAG = "SearchManager"
    private const val MAX_HISTORY = 20

    private val searchHistory = mutableListOf<String>()
    private val searchSuggestions = mutableListOf<String>()

    /**
     * 添加搜索历史。
     *
     * @param keyword 搜索关键词
     */
    fun addSearchHistory(keyword: String) {
        if (keyword.isBlank()) return
        searchHistory.remove(keyword)
        searchHistory.add(0, keyword)
        if (searchHistory.size > MAX_HISTORY) {
            searchHistory.removeAt(searchHistory.size - 1)
        }
        Log.d(TAG, "Search history added: $keyword")
    }

    /**
     * 获取搜索历史。
     *
     * @return 搜索历史列表
     */
    fun getSearchHistory(): List<String> {
        return searchHistory.toList()
    }

    /**
     * 删除搜索历史。
     *
     * @param keyword 搜索关键词
     */
    fun removeSearchHistory(keyword: String) {
        searchHistory.remove(keyword)
        Log.d(TAG, "Search history removed: $keyword")
    }

    /**
     * 清除搜索历史。
     */
    fun clearSearchHistory() {
        searchHistory.clear()
        Log.d(TAG, "Search history cleared")
    }

    /**
     * 添加搜索建议。
     *
     * @param suggestion 搜索建议
     */
    fun addSuggestion(suggestion: String) {
        if (suggestion.isBlank()) return
        if (!searchSuggestions.contains(suggestion)) {
            searchSuggestions.add(suggestion)
        }
    }

    /**
     * 获取搜索建议。
     *
     * @param prefix 前缀
     * @return 匹配的建议列表
     */
    fun getSuggestions(prefix: String): List<String> {
        if (prefix.isBlank()) return emptyList()
        return searchSuggestions.filter { it.startsWith(prefix, ignoreCase = true) }.take(10)
    }

    /**
     * 清除所有建议。
     */
    fun clearSuggestions() {
        searchSuggestions.clear()
    }

    /**
     * 搜索历史数量。
     */
    fun historyCount(): Int = searchHistory.size
}
