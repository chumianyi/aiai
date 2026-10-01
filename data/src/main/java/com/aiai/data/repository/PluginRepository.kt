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

import com.aiai.data.entity.PluginEntity
import kotlinx.coroutines.flow.Flow

/** 插件仓库接口 */
interface PluginRepository {
    fun getPlugins(): Flow<List<PluginEntity>>
    fun getEnabledPlugins(): Flow<List<PluginEntity>>
    suspend fun savePlugin(plugin: PluginEntity)
    suspend fun updatePlugin(plugin: PluginEntity)
    suspend fun deletePlugin(id: String)
    suspend fun setPluginEnabled(id: String, enabled: Boolean)
    suspend fun getPluginById(id: String): PluginEntity?
    suspend fun getEnabledPluginCount(): Int
    suspend fun updatePluginConfig(id: String, config: String)
}
