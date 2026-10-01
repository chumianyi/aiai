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
package com.aiai.data.manager

import android.util.Log
import com.aiai.data.dao.PromptTemplateDao
import com.aiai.data.entity.PromptTemplateEntity

/**
 * 提示词模板管理器。
 *
 * 管理提示词模板的分类、收藏、使用统计等功能。
 */
class PromptTemplateManager(
    private val promptTemplateDao: PromptTemplateDao,
) {

    companion object {
        private const val TAG = "PromptTemplateManager"

        // 预设分类
        const val CATEGORY_WRITING = "writing"
        const val CATEGORY_CODING = "coding"
        const val CATEGORY_TRANSLATION = "translation"
        const val CATEGORY_ANALYSIS = "analysis"
        const val CATEGORY_CREATIVE = "creative"
        const val CATEGORY_BUSINESS = "business"
    }

    /**
     * 获取所有分类。
     */
    fun getAllCategories(): List<String> {
        return listOf(
            CATEGORY_WRITING,
            CATEGORY_CODING,
            CATEGORY_TRANSLATION,
            CATEGORY_ANALYSIS,
            CATEGORY_CREATIVE,
            CATEGORY_BUSINESS,
        )
    }

    /**
     * 获取分类显示名称。
     */
    fun getCategoryDisplayName(category: String): String {
        return when (category) {
            CATEGORY_WRITING -> "写作"
            CATEGORY_CODING -> "编程"
            CATEGORY_TRANSLATION -> "翻译"
            CATEGORY_ANALYSIS -> "分析"
            CATEGORY_CREATIVE -> "创意"
            CATEGORY_BUSINESS -> "商务"
            else -> category
        }
    }

    /**
     * 增加使用计数。
     */
    suspend fun incrementUsageCount(templateId: String) {
        promptTemplateDao.incrementUsageCount(templateId)
        Log.d(TAG, "Incremented usage count for: $templateId")
    }

    /**
     * 切换收藏状态。
     */
    suspend fun toggleFavorite(templateId: String, isFavorite: Boolean) {
        promptTemplateDao.setFavorite(templateId, isFavorite)
        Log.d(TAG, "Toggled favorite for $templateId: $isFavorite")
    }

    /**
     * 获取热门模板。
     */
    suspend fun getPopularTemplates(limit: Int = 10): List<PromptTemplateEntity> {
        return promptTemplateDao.getPopularTemplates(limit)
    }

    /**
     * 获取收藏的模板。
     */
    fun getFavoriteTemplates(): List<PromptTemplateEntity> {
        return promptTemplateDao.getFavoriteTemplatesSync()
    }

    /**
     * 按分类获取模板。
     */
    suspend fun getTemplatesByCategory(category: String): List<PromptTemplateEntity> {
        return promptTemplateDao.getTemplatesByCategory(category)
    }

    /**
     * 获取自定义模板。
     */
    fun getCustomTemplates(): List<PromptTemplateEntity> {
        return promptTemplateDao.getCustomTemplatesSync()
    }
}
