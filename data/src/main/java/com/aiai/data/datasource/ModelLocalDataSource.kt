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
package com.aiai.data.datasource

import com.aiai.data.dao.ModelConfigDao
import com.aiai.data.entity.ModelConfigEntity

/** 模型本地数据源 */
class ModelLocalDataSource(
    private val modelConfigDao: ModelConfigDao,
) : LocalDataSource<ModelConfigEntity, String>() {
    override suspend fun getById(id: String): ModelConfigEntity? = modelConfigDao.getByName(id)
    override suspend fun insert(entity: ModelConfigEntity) = modelConfigDao.insert(entity)
    override suspend fun insertAll(entities: List<ModelConfigEntity>) = modelConfigDao.insertAll(entities)
    override suspend fun update(entity: ModelConfigEntity) = modelConfigDao.update(entity)
    override suspend fun delete(entity: ModelConfigEntity) = modelConfigDao.delete(entity)
    override suspend fun deleteById(id: String) = modelConfigDao.deleteById(id)
    override suspend fun getAll(): List<ModelConfigEntity> = emptyList()
    override suspend fun clear() {}
}
