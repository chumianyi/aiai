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
 * 插件表实体。
 *
 * 存储已安装的插件信息。
 *
 * @property id 插件ID
 * @property name 插件名称
 * @property description 插件描述
 * @property icon 插件图标URL
 * @property packageName 包名
 * @property version 版本号
 * @property isEnabled 是否启用
 * @property configJson 插件配置JSON
 * @property installedAt 安装时间戳
 */
@Entity(
    tableName = "plugins",
    indices = [
        Index(value = ["isEnabled"]),
        Index(value = ["packageName"]),
    ],
)
data class PluginEntity(
    @PrimaryKey
    val id: String,
    val name: String = "",
    val description: String = "",
    val icon: String = "",
    val packageName: String = "",
    val version: String = "1.0.0",
    val isEnabled: Boolean = false,
    val configJson: String = "{}",
    val installedAt: Long = System.currentTimeMillis(),
)
