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
 * 分页响应模型。
 *
 * @property items 数据列表
 * @property total 总记录数
 * @property page 当前页码
 * @property pageSize 每页数量
 * @property totalPages 总页数
 * @property hasNext 是否有下一页
 * @property hasPrev 是否有上一页
 */
data class PagedResponse<T>(
    @SerializedName("items")
    val items: List<T> = emptyList(),

    @SerializedName("total")
    val total: Int = 0,

    @SerializedName("page")
    val page: Int = 1,

    @SerializedName("page_size")
    val pageSize: Int = 20,

    @SerializedName("total_pages")
    val totalPages: Int = 0,

    @SerializedName("has_next")
    val hasNext: Boolean = false,

    @SerializedName("has_prev")
    val hasPrev: Boolean = false,
) {
    /**
     * 是否为空。
     *
     * @return true如果列表为空
     */
    fun isEmpty(): Boolean = items.isEmpty()

    /**
     * 是否为最后一页。
     *
     * @return true如果没有下一页
     */
    fun isLastPage(): Boolean = !hasNext

    companion object {
        /**
         * 创建空分页。
         *
         * @param pageSize 每页数量
         * @return 空分页响应
         */
        fun <T> empty(pageSize: Int = 20): PagedResponse<T> = PagedResponse(
            items = emptyList(),
            total = 0,
            page = 1,
            pageSize = pageSize,
        )

        /**
         * 创建单页结果。
         *
         * @param items 数据列表
         * @return 分页响应
         */
        fun <T> singlePage(items: List<T>): PagedResponse<T> = PagedResponse(
            items = items,
            total = items.size,
            page = 1,
            pageSize = items.size,
            totalPages = 1,
            hasNext = false,
            hasPrev = false,
        )
    }
}
