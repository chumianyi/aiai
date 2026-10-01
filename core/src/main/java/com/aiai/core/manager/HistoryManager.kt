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
 * 历史记录管理器。
 *
 * 提供搜索历史、浏览历史、操作历史等功能。
 */
object HistoryManager {

    private const val TAG = "HistoryManager"
    private const val MAX_HISTORY_SIZE = 100

    private val searchHistory = mutableListOf<String>()
    private val browseHistory = mutableListOf<String>()
    private val actionHistory = mutableListOf<ActionRecord>()

    /**
     * 操作记录数据类。
     *
     * @property action 操作类型
     * @property target 操作目标
     * @property timestamp 时间戳
     */
    data class ActionRecord(
        val action: String,
        val target: String,
        val timestamp: Long = System.currentTimeMillis()
    )

    /**
     * 添加搜索历史。
     *
     * @param keyword 搜索关键词
     */
    fun addSearchHistory(keyword: String) {
        if (keyword.isBlank()) return
        searchHistory.remove(keyword)
        searchHistory.add(0, keyword)
        if (searchHistory.size > MAX_HISTORY_SIZE) {
            searchHistory.removeAt(searchHistory.size - 1)
        }
        Log.d(TAG, "Added search history: $keyword")
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
        Log.d(TAG, "Removed search history: $keyword")
    }

    /**
     * 清除搜索历史。
     */
    fun clearSearchHistory() {
        searchHistory.clear()
        Log.d(TAG, "Search history cleared")
    }

    /**
     * 添加浏览历史。
     *
     * @param url 浏览地址
     */
    fun addBrowseHistory(url: String) {
        if (url.isBlank()) return
        browseHistory.remove(url)
        browseHistory.add(0, url)
        if (browseHistory.size > MAX_HISTORY_SIZE) {
            browseHistory.removeAt(browseHistory.size - 1)
        }
        Log.d(TAG, "Added browse history: $url")
    }

    /**
     * 获取浏览历史。
     *
     * @return 浏览历史列表
     */
    fun getBrowseHistory(): List<String> {
        return browseHistory.toList()
    }

    /**
     * 清除浏览历史。
     */
    fun clearBrowseHistory() {
        browseHistory.clear()
        Log.d(TAG, "Browse history cleared")
    }

    /**
     * 添加操作记录。
     *
     * @param action 操作类型
     * @param target 操作目标
     */
    fun addAction(action: String, target: String) {
        actionHistory.add(0, ActionRecord(action, target))
        if (actionHistory.size > MAX_HISTORY_SIZE) {
            actionHistory.removeAt(actionHistory.size - 1)
        }
        Log.d(TAG, "Action recorded: $action -> $target")
    }

    /**
     * 获取操作历史。
     *
     * @return 操作历史列表
     */
    fun getActionHistory(): List<ActionRecord> {
        return actionHistory.toList()
    }

    /**
     * 清除所有历史。
     */
    fun clearAll() {
        searchHistory.clear()
        browseHistory.clear()
        actionHistory.clear()
        Log.d(TAG, "All history cleared")
    }

    /**
     * 获取历史总数。
     */
    fun totalCount(): Int {
        return searchHistory.size + browseHistory.size + actionHistory.size
    }
}
