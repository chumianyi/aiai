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
import com.aiai.data.entity.PromptTemplateEntity
import kotlinx.coroutines.flow.Flow

/**
 * 提示词模板DAO接口。
 */
@Dao
interface PromptTemplateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(template: PromptTemplateEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(templates: List<PromptTemplateEntity>)

    @Update
    suspend fun update(template: PromptTemplateEntity)

    @Delete
    suspend fun delete(template: PromptTemplateEntity)

    @Query("DELETE FROM prompt_templates WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT * FROM prompt_templates ORDER BY isFavorite DESC, usageCount DESC")
    fun getAllTemplates(): Flow<List<PromptTemplateEntity>>

    @Query("SELECT * FROM prompt_templates WHERE category = :category ORDER BY usageCount DESC")
    fun getTemplatesByCategory(category: String): Flow<List<PromptTemplateEntity>>

    @Query("SELECT * FROM prompt_templates WHERE isFavorite = 1 ORDER BY updatedAt DESC")
    fun getFavoriteTemplates(): Flow<List<PromptTemplateEntity>>

    @Query("SELECT * FROM prompt_templates WHERE isCustom = 1 ORDER BY updatedAt DESC")
    fun getCustomTemplates(): Flow<List<PromptTemplateEntity>>

    @Query("SELECT * FROM prompt_templates WHERE title LIKE '%' || :keyword || '%' OR content LIKE '%' || :keyword || '%'")
    fun searchTemplates(keyword: String): Flow<List<PromptTemplateEntity>>

    @Query("UPDATE prompt_templates SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun setFavorite(id: String, isFavorite: Boolean)

    @Query("UPDATE prompt_templates SET usageCount = usageCount + 1 WHERE id = :id")
    suspend fun incrementUsage(id: String)

    @Query("SELECT COUNT(*) FROM prompt_templates")
    suspend fun getCount(): Int
}
