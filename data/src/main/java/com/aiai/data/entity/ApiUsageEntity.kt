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
 * API使用记录表实体。
 *
 * 记录每次API调用的token消耗和费用统计。
 *
 * @property id 记录ID
 * @property modelName 模型名称
 * @property promptTokens 提示词token数
 * @property completionTokens 生成token数
 * @property totalTokens 总token数
 * @property cost 费用（美元）
 * @property requestTime 请求时间戳
 * @property responseTime 响应时间戳
 * @property status 请求状态（success/failed）
 */
@Entity(
    tableName = "api_usage",
    indices = [
        Index(value = ["modelName"]),
        Index(value = ["requestTime"]),
        Index(value = ["status"]),
    ],
)
data class ApiUsageEntity(
    @PrimaryKey
    val id: String,
    val modelName: String = "",
    val promptTokens: Int = 0,
    val completionTokens: Int = 0,
    val totalTokens: Int = 0,
    val cost: Double = 0.0,
    val requestTime: Long = System.currentTimeMillis(),
    val responseTime: Long = 0L,
    val status: String = "success",
)
