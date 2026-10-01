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
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 聊天消息表实体。
 *
 * 存储会话中的每条消息记录，包括用户消息和AI回复。
 *
 * @property id 消息唯一ID
 * @property conversationId 所属会话ID
 * @property role 消息角色（system/user/assistant/tool）
 * @property content 消息内容
 * @property messageType 消息类型（text/image/audio/file）
 * @property status 消息状态（sending/sent/failed/deleted）
 * @property createdAt 创建时间戳
 * @property updatedAt 更新时间戳
 * @property tokenCount 消耗的token数
 * @property modelName 使用的模型名称
 * @property extraJson 扩展数据JSON
 */
@Entity(
    tableName = "messages",
    foreignKeys = [ForeignKey(
        entity = ConversationEntity::class,
        parentColumns = ["id"],
        childColumns = ["conversationId"],
        onDelete = ForeignKey.CASCADE,
    )],
    indices = [
        Index(value = ["conversationId"]),
        Index(value = ["status"]),
        Index(value = ["createdAt"]),
    ],
)
data class MessageEntity(
    @PrimaryKey
    val id: String,
    val conversationId: String,
    val role: String = "user",
    val content: String = "",
    val messageType: String = "text",
    val status: String = "sent",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val tokenCount: Int = 0,
    val modelName: String = "gpt-4o",
    val extraJson: String = "",
)
