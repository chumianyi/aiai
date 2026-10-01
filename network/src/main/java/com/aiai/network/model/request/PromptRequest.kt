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
package com.aiai.network.model.request

import com.google.gson.annotations.SerializedName

/**
 * 提示词模板请求模型。
 *
 * 用于创建、更新提示词模板。
 *
 * @property title 模板标题
 * @property content 模板内容（支持变量占位符）
 * @property category 分类
 * @property tags 标签列表
 * @property isCustom 是否自定义模板
 * @property isFavorite 是否收藏
 * @property usageCount 使用次数
 * @property description 模板描述
 */
data class PromptRequest(
    @SerializedName("title")
    val title: String = "",

    @SerializedName("content")
    val content: String = "",

    @SerializedName("category")
    val category: String = "general",

    @SerializedName("tags")
    val tags: List<String> = emptyList(),

    @SerializedName("is_custom")
    val isCustom: Boolean = true,

    @SerializedName("is_favorite")
    val isFavorite: Boolean = false,

    @SerializedName("usage_count")
    val usageCount: Int = 0,

    @SerializedName("description")
    val description: String = "",

    @SerializedName("variables")
    val variables: List<String> = emptyList(),
) {
    init {
        require(title.isNotBlank()) { "title must not be blank" }
        require(content.isNotBlank()) { "content must not be blank" }
        require(title.length <= 50) { "title must not exceed 50 characters" }
    }
}
