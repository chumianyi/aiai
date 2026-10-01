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
package com.aiai.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 提示词模板表实体。
 *
 * 存储系统预置和用户自定义的提示词模板。
 *
 * @property id 模板ID
 * @property title 模板标题
 * @property content 模板内容
 * @property category 分类
 * @property tags 标签（JSON数组）
 * @property isCustom 是否自定义
 * @property isFavorite 是否收藏
 * @property usageCount 使用次数
 * @property createdAt 创建时间戳
 * @property updatedAt 更新时间戳
 */
@Entity(
    tableName = "prompt_templates",
    indices = [
        Index(value = ["category"]),
        Index(value = ["isCustom"]),
        Index(value = ["isFavorite"]),
    ],
)
data class PromptTemplateEntity(
    @PrimaryKey
    val id: String,
    val title: String = "",
    val content: String = "",
    val category: String = "general",
    val tags: String = "[]",
    val isCustom: Boolean = false,
    val isFavorite: Boolean = false,
    val usageCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)
