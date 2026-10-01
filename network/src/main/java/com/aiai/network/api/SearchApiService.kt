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
package com.aiai.network.api

import com.aiai.network.model.request.SearchRequest
import com.aiai.network.model.response.ApiResponse
import com.aiai.network.model.response.SearchResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * 搜索API服务接口。
 *
 * 提供对话内搜索和网络搜索功能。
 *
 * 接口列表：
 * - POST /search/web - 网络搜索
 * - POST /search/chat - 对话历史搜索
 * - GET /search/hot - 热门搜索
 */
interface SearchApiService {

    /**
     * 网络搜索。
     *
     * @param request 搜索请求
     * @return 搜索结果
     */
    @POST("search/web")
    suspend fun webSearch(@Body request: SearchRequest): ApiResponse<SearchResponse>

    /**
     * 对话历史搜索。
     *
     * @param request 搜索请求
     * @return 匹配的历史消息
     */
    @POST("search/chat")
    suspend fun chatSearch(@Body request: SearchRequest): ApiResponse<SearchResponse>

    /**
     * 获取热门搜索词。
     *
     * @param limit 返回数量
     * @return 热门搜索词列表
     */
    @GET("search/hot")
    suspend fun getHotSearch(@Query("limit") limit: Int = 10): ApiResponse<List<String>>

    /**
     * 获取搜索建议。
     *
     * @param keyword 输入关键词
     * @return 搜索建议列表
     */
    @GET("search/suggest")
    suspend fun getSearchSuggestions(
        @Query("keyword") keyword: String,
    ): ApiResponse<List<String>>

    /**
     * 清除搜索历史。
     *
     * @return 操作结果
     */
    @POST("search/history/clear")
    suspend fun clearSearchHistory(): ApiResponse<Unit>

    /**
     * 获取搜索历史。
     *
     * @return 搜索历史列表
     */
    @GET("search/history")
    suspend fun getSearchHistory(): ApiResponse<List<String>>

    /**
     * 智能问答搜索。
     *
     * @param request 搜索请求
     * @return AI总结的搜索答案
     */
    @POST("search/qa")
    suspend fun searchQa(@Body request: SearchRequest): ApiResponse<SearchResponse>

    /**
     * 学术搜索。
     *
     * @param request 搜索请求
     * @return 学术论文搜索结果
     */
    @POST("search/academic")
    suspend fun academicSearch(@Body request: SearchRequest): ApiResponse<SearchResponse>
}
