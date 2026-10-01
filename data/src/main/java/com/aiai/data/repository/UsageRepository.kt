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

import com.aiai.data.entity.ApiUsageEntity
import kotlinx.coroutines.flow.Flow

/** 使用统计仓库接口 */
interface UsageRepository {
    fun getUsages(): Flow<List<ApiUsageEntity>>
    suspend fun recordUsage(usage: ApiUsageEntity)
    suspend fun getTotalTokens(startTime: Long, endTime: Long): Long
    suspend fun getTotalCost(startTime: Long, endTime: Long): Double
    suspend fun getUsageByModel(): List<Map<String, Any>>
    suspend fun getFailedCount(): Int
    suspend fun getTotalCount(): Int
    suspend fun cleanupOldData(beforeTime: Long)
}
