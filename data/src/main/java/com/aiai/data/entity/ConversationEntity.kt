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
 * 会话表实体。
 *
 * 存储每个对话会话的配置和元信息。
 *
 * @property id 会话唯一ID
 * @property title 会话标题
 * @property modelName 使用的模型名称
 * @property systemPrompt 系统提示词
 * @property temperature 温度参数
 * @property topP 核采样参数
 * @property maxTokens 最大输出token数
 * @property isPinned 是否置顶
 * @property isArchived 是否归档
 * @property messageCount 消息数量
 * @property lastMessageId 最后一条消息ID
 * @property createdAt 创建时间戳
 * @property updatedAt 更新时间戳
 */
@Entity(
    tableName = "conversations",
    indices = [
        Index(value = ["isPinned"]),
        Index(value = ["isArchived"]),
        Index(value = ["updatedAt"]),
    ],
)
data class ConversationEntity(
    @PrimaryKey
    val id: String,
    val title: String = "新对话",
    val modelName: String = "gpt-4o",
    val systemPrompt: String = "",
    val temperature: Double = 0.7,
    val topP: Double = 1.0,
    val maxTokens: Int = 4096,
    val isPinned: Boolean = false,
    val isArchived: Boolean = false,
    val messageCount: Int = 0,
    val lastMessageId: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)
