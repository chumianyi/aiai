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
 * 导出任务表实体。
 *
 * 存储会话导出任务的状态和结果。
 *
 * @property id 任务ID
 * @property conversationId 关联会话ID
 * @property exportFormat 导出格式（markdown/pdf/html/json）
 * @property filePath 导出文件路径
 * @property status 任务状态（pending/processing/completed/failed）
 * @property createdAt 创建时间戳
 * @property completedAt 完成时间戳
 */
@Entity(
    tableName = "export_tasks",
    indices = [
        Index(value = ["conversationId"]),
        Index(value = ["status"]),
    ],
)
data class ExportTaskEntity(
    @PrimaryKey
    val id: String,
    val conversationId: String = "",
    val exportFormat: String = "markdown",
    val filePath: String = "",
    val status: String = "pending",
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long = 0L,
)
