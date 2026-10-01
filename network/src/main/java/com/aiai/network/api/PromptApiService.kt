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

import com.aiai.network.model.request.PromptRequest
import com.aiai.network.model.response.ApiResponse
import com.aiai.network.model.response.PagedResponse
import com.aiai.network.model.response.PromptResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 提示词模板API服务接口。
 *
 * 提供提示词模板的列表查询、详情获取、收藏管理、自定义模板创建等功能。
 *
 * 接口列表：
 * - GET /prompt/templates - 模板列表
 * - GET /prompt/template/{id} - 模板详情
 * - POST /prompt/template/{id}/favorite - 收藏/取消收藏
 * - POST /prompt/custom - 创建自定义模板
 */
interface PromptApiService {

    /**
     * 获取提示词模板列表。
     *
     * @param category 分类（writing/coding/translation/general）
     * @param page 页码
     * @param pageSize 每页数量
     * @return 分页模板列表
     */
    @GET("prompt/templates")
    suspend fun getPromptTemplates(
        @Query("category") category: String = "all",
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20,
    ): ApiResponse<PagedResponse<PromptResponse>>

    /**
     * 获取模板详情。
     *
     * @param templateId 模板ID
     * @return 模板详情
     */
    @GET("prompt/template/{templateId}")
    suspend fun getPromptDetail(@Path("templateId") templateId: String): ApiResponse<PromptResponse>

    /**
     * 收藏/取消收藏模板。
     *
     * @param templateId 模板ID
     * @param favorite 是否收藏
     * @return 操作结果
     */
    @POST("prompt/template/{templateId}/favorite")
    suspend fun toggleFavorite(
        @Path("templateId") templateId: String,
        @Query("favorite") favorite: Boolean,
    ): ApiResponse<Unit>

    /**
     * 创建自定义模板。
     *
     * @param request 自定义模板请求
     * @return 创建的模板
     */
    @POST("prompt/custom")
    suspend fun createCustomPrompt(@Body request: PromptRequest): ApiResponse<PromptResponse>

    /**
     * 更新自定义模板。
     *
     * @param templateId 模板ID
     * @param request 更新请求
     * @return 更新后的模板
     */
    @PUT("prompt/template/{templateId}")
    suspend fun updateCustomPrompt(
        @Path("templateId") templateId: String,
        @Body request: PromptRequest,
    ): ApiResponse<PromptResponse>

    /**
     * 删除自定义模板。
     *
     * @param templateId 模板ID
     * @return 操作结果
     */
    @DELETE("prompt/template/{templateId}")
    suspend fun deleteCustomPrompt(@Path("templateId") templateId: String): ApiResponse<Unit>

    /**
     * 获取收藏的模板列表。
     *
     * @return 收藏模板列表
     */
    @GET("prompt/favorites")
    suspend fun getFavoritePrompts(): ApiResponse<List<PromptResponse>>

    /**
     * 搜索提示词模板。
     *
     * @param keyword 搜索关键词
     * @return 搜索结果
     */
    @GET("prompt/search")
    suspend fun searchPrompts(
        @Query("keyword") keyword: String,
    ): ApiResponse<List<PromptResponse>>

    /**
     * 增加模板使用计数。
     *
     * @param templateId 模板ID
     * @return 操作结果
     */
    @POST("prompt/template/{templateId}/use")
    suspend fun incrementUsage(@Path("templateId") templateId: String): ApiResponse<Unit>

    /**
     * 获取推荐提示词。
     *
     * @param scene 使用场景
     * @return 推荐模板列表
     */
    @GET("prompt/recommended")
    suspend fun getRecommendedPrompts(
        @Query("scene") scene: String = "general",
    ): ApiResponse<List<PromptResponse>>
}
