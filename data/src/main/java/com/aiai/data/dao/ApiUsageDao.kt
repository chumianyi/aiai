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
import com.aiai.data.entity.ApiUsageEntity
import kotlinx.coroutines.flow.Flow

/**
 * API使用记录DAO接口。
 */
@Dao
interface ApiUsageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(usage: ApiUsageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(usages: List<ApiUsageEntity>)

    @Query("SELECT * FROM api_usage ORDER BY requestTime DESC")
    fun getAllUsages(): Flow<List<ApiUsageEntity>>

    @Query("SELECT * FROM api_usage WHERE requestTime BETWEEN :startTime AND :endTime ORDER BY requestTime DESC")
    suspend fun getUsagesByTimeRange(startTime: Long, endTime: Long): List<ApiUsageEntity>

    @Query("SELECT * FROM api_usage WHERE modelName = :modelName ORDER BY requestTime DESC")
    fun getUsagesByModel(modelName: String): Flow<List<ApiUsageEntity>>

    @Query("SELECT SUM(totalTokens) FROM api_usage WHERE requestTime BETWEEN :startTime AND :endTime")
    suspend fun getTotalTokensInRange(startTime: Long, endTime: Long): Long?

    @Query("SELECT SUM(cost) FROM api_usage WHERE requestTime BETWEEN :startTime AND :endTime")
    suspend fun getTotalCostInRange(startTime: Long, endTime: Long): Double?

    @Query("SELECT modelName, SUM(totalTokens) as totalTokens FROM api_usage GROUP BY modelName ORDER BY totalTokens DESC")
    suspend fun getUsageByModelStats(): List<ModelUsageStat>

    @Query("SELECT COUNT(*) FROM api_usage WHERE status = 'failed'")
    suspend fun getFailedCount(): Int

    @Query("SELECT COUNT(*) FROM api_usage")
    suspend fun getTotalCount(): Int

    @Query("DELETE FROM api_usage WHERE requestTime < :beforeTime")
    suspend fun deleteBefore(beforeTime: Long)
}

/**
 * 模型使用统计。
 */
data class ModelUsageStat(
    val modelName: String,
    val totalTokens: Long,
)
