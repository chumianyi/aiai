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
import com.aiai.data.entity.ModelConfigEntity
import kotlinx.coroutines.flow.Flow

/**
 * 模型配置DAO接口。
 */
@Dao
interface ModelConfigDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(config: ModelConfigEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(configs: List<ModelConfigEntity>)

    @Update
    suspend fun update(config: ModelConfigEntity)

    @Delete
    suspend fun delete(config: ModelConfigEntity)

    @Query("DELETE FROM model_configs WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT * FROM model_configs ORDER BY isDefault DESC, createdAt ASC")
    fun getAllConfigs(): Flow<List<ModelConfigEntity>>

    @Query("SELECT * FROM model_configs WHERE isActive = 1 ORDER BY createdAt ASC")
    fun getActiveConfigs(): Flow<List<ModelConfigEntity>>

    @Query("SELECT * FROM model_configs WHERE isDefault = 1 LIMIT 1")
    suspend fun getDefaultConfig(): ModelConfigEntity?

    @Query("UPDATE model_configs SET isDefault = 0")
    suspend fun clearDefault()

    @Query("UPDATE model_configs SET isDefault = 1 WHERE id = :id")
    suspend fun setDefault(id: String)

    @Query("UPDATE model_configs SET isActive = :isActive WHERE id = :id")
    suspend fun setActive(id: String, isActive: Boolean)

    @Query("SELECT * FROM model_configs WHERE modelName = :modelName LIMIT 1")
    suspend fun getByName(modelName: String): ModelConfigEntity?

    @Query("SELECT COUNT(*) FROM model_configs")
    suspend fun getCount(): Int
}
