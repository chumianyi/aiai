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
package com.aiai.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aiai.data.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

/**
 * 搜索历史DAO接口。
 */
@Dao
interface SearchHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: SearchHistoryEntity)

    @Query("SELECT * FROM search_history ORDER BY createdAt DESC LIMIT :limit")
    fun getRecentHistory(limit: Int = 20): Flow<List<SearchHistoryEntity>>

    @Query("SELECT * FROM search_history WHERE searchType = :type ORDER BY createdAt DESC")
    fun getHistoryByType(type: String): Flow<List<SearchHistoryEntity>>

    @Query("DELETE FROM search_history WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM search_history")
    suspend fun clearAll()

    @Query("DELETE FROM search_history WHERE searchType = :type")
    suspend fun clearByType(type: String)

    @Query("SELECT keyword, COUNT(*) as count FROM search_history GROUP BY keyword ORDER BY count DESC LIMIT :limit")
    suspend fun getHotSearches(limit: Int = 10): List<HotSearchResult>

    @Query("SELECT COUNT(*) FROM search_history")
    suspend fun getCount(): Int

    @Query("SELECT * FROM search_history WHERE keyword LIKE '%' || :keyword || '%' LIMIT :limit")
    suspend fun searchByKeyword(keyword: String, limit: Int = 10): List<SearchHistoryEntity>
}

/**
 * 热门搜索结果。
 */
data class HotSearchResult(
    val keyword: String,
    val count: Int,
)
