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

import com.aiai.network.model.request.ConversationRequest
import com.aiai.network.model.response.ApiResponse
import com.aiai.network.model.response.ConversationResponse
import com.aiai.network.model.response.PagedResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 会话管理API服务接口。
 *
 * 提供会话的创建、列表查询、重命名、删除、置顶等管理功能。
 *
 * 接口列表：
 * - POST /conversation/create - 创建会话
 * - GET /conversation/list - 会话列表
 * - PUT /conversation/{id}/rename - 重命名会话
 * - DELETE /conversation/{id} - 删除会话
 * - PUT /conversation/{id}/pin - 置顶/取消置顶
 * - PUT /conversation/{id}/archive - 归档/取消归档
 */
interface ConversationApiService {

    /**
     * 创建新会话。
     *
     * @param request 会话创建请求
     * @return 创建的会话信息
     */
    @POST("conversation/create")
    suspend fun createConversation(@Body request: ConversationRequest): ApiResponse<ConversationResponse>

    /**
     * 获取会话列表。
     *
     * @param page 页码
     * @param pageSize 每页数量
     * @param includeArchived 是否包含归档会话
     * @return 分页会话列表
     */
    @GET("conversation/list")
    suspend fun getConversationList(
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20,
        @Query("include_archived") includeArchived: Boolean = false,
    ): ApiResponse<PagedResponse<ConversationResponse>>

    /**
     * 重命名会话。
     *
     * @param conversationId 会话ID
     * @param request 重命名请求（包含新标题）
     * @return 更新后的会话信息
     */
    @PUT("conversation/{conversationId}/rename")
    suspend fun renameConversation(
        @Path("conversationId") conversationId: String,
        @Body request: ConversationRequest,
    ): ApiResponse<ConversationResponse>

    /**
     * 删除会话。
     *
     * @param conversationId 会话ID
     * @return 操作结果
     */
    @DELETE("conversation/{conversationId}")
    suspend fun deleteConversation(@Path("conversationId") conversationId: String): ApiResponse<Unit>

    /**
     * 置顶/取消置顶会话。
     *
     * @param conversationId 会话ID
     * @param pinned 是否置顶
     * @return 更新后的会话信息
     */
    @PUT("conversation/{conversationId}/pin")
    suspend fun pinConversation(
        @Path("conversationId") conversationId: String,
        @Query("pinned") pinned: Boolean,
    ): ApiResponse<ConversationResponse>

    /**
     * 归档/取消归档会话。
     *
     * @param conversationId 会话ID
     * @param archived 是否归档
     * @return 更新后的会话信息
     */
    @PUT("conversation/{conversationId}/archive")
    suspend fun archiveConversation(
        @Path("conversationId") conversationId: String,
        @Query("archived") archived: Boolean,
    ): ApiResponse<ConversationResponse>

    /**
     * 获取单个会话详情。
     *
     * @param conversationId 会话ID
     * @return 会话详情
     */
    @GET("conversation/{conversationId}")
    suspend fun getConversationDetail(@Path("conversationId") conversationId: String): ApiResponse<ConversationResponse>

    /**
     * 更新会话配置。
     *
     * @param conversationId 会话ID
     * @param request 会话配置更新请求
     * @return 更新后的会话信息
     */
    @PUT("conversation/{conversationId}/config")
    suspend fun updateConversationConfig(
        @Path("conversationId") conversationId: String,
        @Body request: ConversationRequest,
    ): ApiResponse<ConversationResponse>

    /**
     * 批量删除会话。
     *
     * @param conversationIds 会话ID列表
     * @return 操作结果
     */
    @DELETE("conversation/batch-delete")
    suspend fun batchDeleteConversations(@Query("ids") conversationIds: List<String>): ApiResponse<Unit>

    /**
     * 搜索会话。
     *
     * @param keyword 搜索关键词
     * @param page 页码
     * @param pageSize 每页数量
     * @return 搜索结果
     */
    @GET("conversation/search")
    suspend fun searchConversations(
        @Query("keyword") keyword: String,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20,
    ): ApiResponse<PagedResponse<ConversationResponse>>
}
