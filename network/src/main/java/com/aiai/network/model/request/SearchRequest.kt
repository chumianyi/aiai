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
 * 搜索请求模型。
 *
 * 用于网络搜索和对话历史搜索的API请求。
 *
 * @property keyword 搜索关键词
 * @property searchType 搜索类型（web/chat/academic/image）
 * @param page 页码
 * @property pageSize 每页数量
 * @property filters 搜索过滤器
 * @property language 搜索语言
 * @property timeRange 时间范围（day/week/month/year/any）
 * @property sortBy 排序方式（relevance/time）
 */
data class SearchRequest(
    @SerializedName("keyword")
    val keyword: String = "",

    @SerializedName("search_type")
    val searchType: String = "web",

    @SerializedName("page")
    val page: Int = 1,

    @SerializedName("page_size")
    val pageSize: Int = 20,

    @SerializedName("filters")
    val filters: Map<String, Any> = emptyMap(),

    @SerializedName("language")
    val language: String = "zh-CN",

    @SerializedName("time_range")
    val timeRange: String = "any",

    @SerializedName("sort_by")
    val sortBy: String = "relevance",
) {
    init {
        require(keyword.isNotBlank()) { "keyword must not be blank" }
        require(page > 0) { "page must be positive" }
        require(pageSize in 1..100) { "page_size must be between 1 and 100" }
    }
}
