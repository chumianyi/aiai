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
 * 模型配置表实体。
 *
 * 存储用户配置的各种AI模型参数。
 *
 * @property id 配置唯一ID
 * @property name 配置名称
 * @property baseUrl API基础URL
 * @property apiKey API密钥
 * @property modelName 模型名称
 * @property temperature 默认温度
 * @property topP 默认top_p
 * @property maxTokens 默认最大token数
 * @property isDefault 是否为默认模型
 * @property isActive 是否激活
 * @property createdAt 创建时间戳
 */
@Entity(
    tableName = "model_configs",
    indices = [
        Index(value = ["isDefault"]),
        Index(value = ["isActive"]),
        Index(value = ["modelName"]),
    ],
)
data class ModelConfigEntity(
    @PrimaryKey
    val id: String,
    val name: String = "默认模型",
    val baseUrl: String = "https://api.aiai.com/",
    val apiKey: String = "",
    val modelName: String = "gpt-4o",
    val temperature: Double = 0.7,
    val topP: Double = 1.0,
    val maxTokens: Int = 4096,
    val isDefault: Boolean = false,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
)
