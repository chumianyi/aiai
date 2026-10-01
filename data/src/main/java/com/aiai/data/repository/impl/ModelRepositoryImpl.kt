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

import com.aiai.data.dao.ModelConfigDao
import com.aiai.data.entity.ModelConfigEntity
import com.aiai.data.repository.ModelRepository
import com.aiai.network.api.ModelApiService
import kotlinx.coroutines.flow.Flow

/** 模型仓库实现 */
class ModelRepositoryImpl(
    private val modelConfigDao: ModelConfigDao,
    private val modelApi: ModelApiService,
) : ModelRepository {

    override fun getModelConfigs(): Flow<List<ModelConfigEntity>> = modelConfigDao.getAllConfigs()
    override fun getActiveModelConfigs(): Flow<List<ModelConfigEntity>> = modelConfigDao.getActiveConfigs()
    override suspend fun getDefaultModel(): ModelConfigEntity? = modelConfigDao.getDefaultConfig()
    override suspend fun saveModelConfig(config: ModelConfigEntity) = modelConfigDao.insert(config)
    override suspend fun updateModelConfig(config: ModelConfigEntity) = modelConfigDao.update(config)
    override suspend fun deleteModelConfig(id: String) = modelConfigDao.deleteById(id)
    override suspend fun setDefaultModel(id: String) {
        modelConfigDao.clearDefault()
        modelConfigDao.setDefault(id)
    }
    override suspend fun setActive(id: String, active: Boolean) = modelConfigDao.setActive(id, active)
    override suspend fun getModelByName(name: String): ModelConfigEntity? = modelConfigDao.getByName(name)
    override suspend fun getModelCount(): Int = modelConfigDao.getCount()
    override suspend fun fetchRemoteModels(): Result<List<ModelConfigEntity>> {
        return try {
            val response = modelApi.getModelList()
            if (response.isSuccessful()) {
                Result.success(emptyList())
            } else {
                Result.failure(Exception("Failed to fetch models"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
