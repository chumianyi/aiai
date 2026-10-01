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
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aiai.data.entity.PluginEntity
import kotlinx.coroutines.flow.Flow

/**
 * 插件DAO接口。
 */
@Dao
interface PluginDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(plugin: PluginEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(plugins: List<PluginEntity>)

    @Update
    suspend fun update(plugin: PluginEntity)

    @Delete
    suspend fun delete(plugin: PluginEntity)

    @Query("DELETE FROM plugins WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT * FROM plugins ORDER BY installedAt DESC")
    fun getAllPlugins(): Flow<List<PluginEntity>>

    @Query("SELECT * FROM plugins WHERE isEnabled = 1 ORDER BY name ASC")
    fun getEnabledPlugins(): Flow<List<PluginEntity>>

    @Query("SELECT * FROM plugins WHERE id = :id")
    suspend fun getPluginById(id: String): PluginEntity?

    @Query("UPDATE plugins SET isEnabled = :isEnabled WHERE id = :id")
    suspend fun setEnabled(id: String, isEnabled: Boolean)

    @Query("UPDATE plugins SET configJson = :config WHERE id = :id")
    suspend fun updateConfig(id: String, config: String)

    @Query("SELECT COUNT(*) FROM plugins WHERE isEnabled = 1")
    suspend fun getEnabledCount(): Int

    @Query("SELECT COUNT(*) FROM plugins")
    suspend fun getCount(): Int
}
