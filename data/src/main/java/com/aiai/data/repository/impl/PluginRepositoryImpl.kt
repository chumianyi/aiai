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

import com.aiai.data.dao.PluginDao
import com.aiai.data.entity.PluginEntity
import com.aiai.data.repository.PluginRepository
import kotlinx.coroutines.flow.Flow

/** 插件仓库实现 */
class PluginRepositoryImpl(
    private val pluginDao: PluginDao,
) : PluginRepository {

    override fun getPlugins(): Flow<List<PluginEntity>> = pluginDao.getAllPlugins()
    override fun getEnabledPlugins(): Flow<List<PluginEntity>> = pluginDao.getEnabledPlugins()
    override suspend fun savePlugin(plugin: PluginEntity) = pluginDao.insert(plugin)
    override suspend fun updatePlugin(plugin: PluginEntity) = pluginDao.update(plugin)
    override suspend fun deletePlugin(id: String) = pluginDao.deleteById(id)
    override suspend fun setPluginEnabled(id: String, enabled: Boolean) = pluginDao.setEnabled(id, enabled)
    override suspend fun getPluginById(id: String): PluginEntity? = pluginDao.getPluginById(id)
    override suspend fun getEnabledPluginCount(): Int = pluginDao.getEnabledCount()
    override suspend fun updatePluginConfig(id: String, config: String) = pluginDao.updateConfig(id, config)
}
