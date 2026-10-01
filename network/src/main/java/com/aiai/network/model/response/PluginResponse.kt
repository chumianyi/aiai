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
package com.aiai.network.model.response

import com.google.gson.annotations.SerializedName

/**
 * 插件响应模型。
 *
 * @property id 插件ID
 * @property name 插件名称
 * @property description 插件描述
 * @property icon 插件图标URL
 * @property packageName 包名
 * @property version 版本号
 * @property isEnabled 是否启用
 * @property configJson 配置JSON
 * @property installedAt 安装时间
 * @property category 插件分类
 * @property rating 评分
 */
data class PluginResponse(
    @SerializedName("id")
    val id: String = "",

    @SerializedName("name")
    val name: String = "",

    @SerializedName("description")
    val description: String = "",

    @SerializedName("icon")
    val icon: String = "",

    @SerializedName("package_name")
    val packageName: String = "",

    @SerializedName("version")
    val version: String = "1.0.0",

    @SerializedName("is_enabled")
    val isEnabled: Boolean = false,

    @SerializedName("config_json")
    val configJson: String = "",

    @SerializedName("installed_at")
    val installedAt: String = "",

    @SerializedName("category")
    val category: String = "general",

    @SerializedName("rating")
    val rating: Double = 0.0,
)
