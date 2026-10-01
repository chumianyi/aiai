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

import com.aiai.data.entity.PromptTemplateEntity
import kotlinx.coroutines.flow.Flow

/** 提示词仓库接口 */
interface PromptRepository {
    fun getPrompts(): Flow<List<PromptTemplateEntity>>
    fun getPromptsByCategory(category: String): Flow<List<PromptTemplateEntity>>
    fun getFavoritePrompts(): Flow<List<PromptTemplateEntity>>
    fun getCustomPrompts(): Flow<List<PromptTemplateEntity>>
    suspend fun savePrompt(prompt: PromptTemplateEntity)
    suspend fun updatePrompt(prompt: PromptTemplateEntity)
    suspend fun deletePrompt(id: String)
    suspend fun setFavorite(id: String, favorite: Boolean)
    suspend fun incrementUsage(id: String)
    fun searchPrompts(keyword: String): Flow<List<PromptTemplateEntity>>
    suspend fun getPromptCount(): Int
}
