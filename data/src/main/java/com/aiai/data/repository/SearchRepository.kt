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
package com.aiai.data.repository

import com.aiai.data.entity.SearchHistoryEntity
import com.aiai.network.model.request.SearchRequest
import com.aiai.network.model.response.SearchResponse
import kotlinx.coroutines.flow.Flow

/** 搜索仓库接口 */
interface SearchRepository {
    suspend fun webSearch(request: SearchRequest): Result<SearchResponse>
    suspend fun chatSearch(request: SearchRequest): Result<SearchResponse>
    fun getSearchHistory(): Flow<List<SearchHistoryEntity>>
    suspend fun saveSearchHistory(keyword: String, type: String)
    suspend fun clearSearchHistory()
    suspend fun getHotSearches(): List<String>
    suspend fun getSearchSuggestions(keyword: String): List<String>
}
