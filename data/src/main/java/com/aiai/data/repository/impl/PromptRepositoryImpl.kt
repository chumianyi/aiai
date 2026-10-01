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

import com.aiai.data.dao.PromptTemplateDao
import com.aiai.data.entity.PromptTemplateEntity
import com.aiai.data.repository.PromptRepository
import kotlinx.coroutines.flow.Flow

/** 提示词仓库实现 */
class PromptRepositoryImpl(
    private val promptDao: PromptTemplateDao,
) : PromptRepository {

    override fun getPrompts(): Flow<List<PromptTemplateEntity>> = promptDao.getAllTemplates()
    override fun getPromptsByCategory(category: String): Flow<List<PromptTemplateEntity>> = promptDao.getTemplatesByCategory(category)
    override fun getFavoritePrompts(): Flow<List<PromptTemplateEntity>> = promptDao.getFavoriteTemplates()
    override fun getCustomPrompts(): Flow<List<PromptTemplateEntity>> = promptDao.getCustomTemplates()
    override suspend fun savePrompt(prompt: PromptTemplateEntity) = promptDao.insert(prompt)
    override suspend fun updatePrompt(prompt: PromptTemplateEntity) = promptDao.update(prompt)
    override suspend fun deletePrompt(id: String) = promptDao.deleteById(id)
    override suspend fun setFavorite(id: String, favorite: Boolean) = promptDao.setFavorite(id, favorite)
    override suspend fun incrementUsage(id: String) = promptDao.incrementUsage(id)
    override fun searchPrompts(keyword: String): Flow<List<PromptTemplateEntity>> = promptDao.searchTemplates(keyword)
    override suspend fun getPromptCount(): Int = promptDao.getCount()
}
