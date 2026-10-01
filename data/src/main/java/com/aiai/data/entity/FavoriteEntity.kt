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
 * 收藏表实体。
 *
 * 存储用户收藏的消息内容。
 *
 * @property id 收藏ID
 * @property messageId 关联消息ID
 * @property conversationId 关联会话ID
 * @property content 收藏内容快照
 * @property note 用户备注
 * @property createdAt 创建时间戳
 * @property category 收藏分类
 */
@Entity(
    tableName = "favorites",
    indices = [
        Index(value = ["messageId"]),
        Index(value = ["conversationId"]),
        Index(value = ["category"]),
    ],
)
data class FavoriteEntity(
    @PrimaryKey
    val id: String,
    val messageId: String = "",
    val conversationId: String = "",
    val content: String = "",
    val note: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val category: String = "general",
)
