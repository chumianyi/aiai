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

import com.aiai.data.entity.ModelConfigEntity
import kotlinx.coroutines.flow.Flow

/** 模型仓库接口 */
interface ModelRepository {
    fun getModelConfigs(): Flow<List<ModelConfigEntity>>
    fun getActiveModelConfigs(): Flow<List<ModelConfigEntity>>
    suspend fun getDefaultModel(): ModelConfigEntity?
    suspend fun saveModelConfig(config: ModelConfigEntity)
    suspend fun updateModelConfig(config: ModelConfigEntity)
    suspend fun deleteModelConfig(id: String)
    suspend fun setDefaultModel(id: String)
    suspend fun setActive(id: String, active: Boolean)
    suspend fun getModelByName(name: String): ModelConfigEntity?
    suspend fun getModelCount(): Int
    suspend fun fetchRemoteModels(): Result<List<ModelConfigEntity>>
}
