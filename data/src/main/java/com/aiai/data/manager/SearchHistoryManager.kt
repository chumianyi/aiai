/*
 * Copyright (c) 2024 爱Ai (AiAi) App. All rights reserved.
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
package com.aiai.data.manager

import android.util.Log
import com.aiai.data.dao.SearchHistoryDao
import com.aiai.data.entity.SearchHistoryEntity

/**
 * 搜索历史管理器。
 *
 * 管理搜索历史记录和热门搜索。
 */
class SearchHistoryManager(
    private val searchHistoryDao: SearchHistoryDao,
) {

    companion object {
        private const val TAG = "SearchHistoryManager"
        private const val MAX_HISTORY_SIZE = 50
    }

    /**
     * 记录搜索历史。
     */
    suspend fun recordSearch(keyword: String, searchType: String = "all") {
        if (keyword.isBlank()) return
        val history = SearchHistoryEntity(
            id = java.util.UUID.randomUUID().toString(),
            keyword = keyword,
            searchType = searchType,
            createdAt = System.currentTimeMillis(),
        )
        searchHistoryDao.insert(history)
        Log.d(TAG, "Recorded search: $keyword")
    }

    /**
     * 获取搜索历史。
     */
    fun getSearchHistory(): List<SearchHistoryEntity> {
        return searchHistoryDao.getRecentHistorySync(MAX_HISTORY_SIZE)
    }

    /**
     * 获取热门搜索。
     */
    fun getPopularSearches(limit: Int = 10): List<String> {
        return searchHistoryDao.getPopularKeywordsSync(limit)
    }

    /**
     * 删除搜索历史。
     */
    suspend fun deleteHistory(historyId: String) {
        searchHistoryDao.deleteById(historyId)
    }

    /**
     * 清空所有搜索历史。
     */
    suspend fun clearAllHistory() {
        searchHistoryDao.deleteAll()
        Log.d(TAG, "Search history cleared")
    }

    /**
     * 按类型获取搜索历史。
     */
    fun getHistoryByType(searchType: String): List<SearchHistoryEntity> {
        return searchHistoryDao.getByTypeSync(searchType)
    }
}
