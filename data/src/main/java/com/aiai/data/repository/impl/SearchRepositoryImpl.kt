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
package com.aiai.data.repository.impl

import com.aiai.data.dao.SearchHistoryDao
import com.aiai.data.entity.SearchHistoryEntity
import com.aiai.data.repository.SearchRepository
import com.aiai.network.api.SearchApiService
import com.aiai.network.model.request.SearchRequest
import com.aiai.network.model.response.SearchResponse
import kotlinx.coroutines.flow.Flow
import java.util.UUID

/** 搜索仓库实现 */
class SearchRepositoryImpl(
    private val searchHistoryDao: SearchHistoryDao,
    private val searchApi: SearchApiService,
) : SearchRepository {

    override suspend fun webSearch(request: SearchRequest): Result<SearchResponse> {
        return try {
            val response = searchApi.webSearch(request)
            if (response.isSuccessful()) Result.success(response.getDataOrThrow())
            else Result.failure(Exception(response.message))
        } catch (e: Exception) { Result.failure(e) }
    }
    override suspend fun chatSearch(request: SearchRequest): Result<SearchResponse> {
        return try {
            val response = searchApi.chatSearch(request)
            if (response.isSuccessful()) Result.success(response.getDataOrThrow())
            else Result.failure(Exception(response.message))
        } catch (e: Exception) { Result.failure(e) }
    }
    override fun getSearchHistory(): Flow<List<SearchHistoryEntity>> = searchHistoryDao.getRecentHistory()
    override suspend fun saveSearchHistory(keyword: String, type: String) {
        searchHistoryDao.insert(SearchHistoryEntity(
            id = UUID.randomUUID().toString(),
            keyword = keyword,
            searchType = type,
        ))
    }
    override suspend fun clearSearchHistory() = searchHistoryDao.clearAll()
    override suspend fun getHotSearches(): List<String> {
        return try {
            val response = searchApi.getHotSearch()
            if (response.isSuccessful()) response.data ?: emptyList()
            else emptyList()
        } catch (e: Exception) { emptyList() }
    }
    override suspend fun getSearchSuggestions(keyword: String): List<String> {
        return try {
            val response = searchApi.getSearchSuggestions(keyword)
            if (response.isSuccessful()) response.data ?: emptyList()
            else emptyList()
        } catch (e: Exception) { emptyList() }
    }
}
