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
 * 搜索响应模型。
 *
 * @property results 搜索结果列表
 * @property total 总结果数
 * @property query 搜索查询词
 * @property searchTimeMs 搜索耗时（毫秒）
 * @property page 当前页码
 * @property pageSize 每页数量
 */
data class SearchResponse(
    @SerializedName("results")
    val results: List<SearchResult> = emptyList(),

    @SerializedName("total")
    val total: Int = 0,

    @SerializedName("query")
    val query: String = "",

    @SerializedName("search_time_ms")
    val searchTimeMs: Long = 0L,

    @SerializedName("page")
    val page: Int = 1,

    @SerializedName("page_size")
    val pageSize: Int = 20,
)

/**
 * 单条搜索结果。
 *
 * @property title 标题
 * @property url 链接URL
 * @property snippet 摘要
 * @property score 相关度评分
 * @property source 来源
 * @property publishedAt 发布时间
 * @property favicon 网站图标URL
 */
data class SearchResult(
    @SerializedName("title")
    val title: String = "",

    @SerializedName("url")
    val url: String = "",

    @SerializedName("snippet")
    val snippet: String = "",

    @SerializedName("score")
    val score: Double = 0.0,

    @SerializedName("source")
    val source: String = "",

    @SerializedName("published_at")
    val publishedAt: String = "",

    @SerializedName("favicon")
    val favicon: String = "",
)
