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
 * 通知表实体。
 *
 * 存储应用内通知消息。
 *
 * @property id 通知ID
 * @property title 通知标题
 * @property content 通知内容
 * @property type 通知类型（system/promotion/update/usage）
 * @property isRead 是否已读
 * @property createdAt 创建时间戳
 * @property actionData 点击动作数据（JSON）
 */
@Entity(
    tableName = "notifications",
    indices = [
        Index(value = ["type"]),
        Index(value = ["isRead"]),
        Index(value = ["createdAt"]),
    ],
)
data class NotificationEntity(
    @PrimaryKey
    val id: String,
    val title: String = "",
    val content: String = "",
    val type: String = "system",
    val isRead: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val actionData: String = "{}",
)
