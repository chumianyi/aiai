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

import com.aiai.data.dao.ApiUsageDao
import com.aiai.data.entity.ApiUsageEntity
import com.aiai.data.repository.UsageRepository
import kotlinx.coroutines.flow.Flow

/** 使用统计仓库实现 */
class UsageRepositoryImpl(
    private val apiUsageDao: ApiUsageDao,
) : UsageRepository {

    override fun getUsages(): Flow<List<ApiUsageEntity>> = apiUsageDao.getAllUsages()
    override suspend fun recordUsage(usage: ApiUsageEntity) = apiUsageDao.insert(usage)
    override suspend fun getTotalTokens(startTime: Long, endTime: Long): Long =
        apiUsageDao.getTotalTokensInRange(startTime, endTime) ?: 0L
    override suspend fun getTotalCost(startTime: Long, endTime: Long): Double =
        apiUsageDao.getTotalCostInRange(startTime, endTime) ?: 0.0
    override suspend fun getUsageByModel(): List<Map<String, Any>> =
        apiUsageDao.getUsageByModelStats().map { mapOf("model" to it.modelName, "tokens" to it.totalTokens) }
    override suspend fun getFailedCount(): Int = apiUsageDao.getFailedCount()
    override suspend fun getTotalCount(): Int = apiUsageDao.getTotalCount()
    override suspend fun cleanupOldData(beforeTime: Long) = apiUsageDao.deleteBefore(beforeTime)
}
