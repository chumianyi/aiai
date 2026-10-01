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

import com.aiai.network.model.request.ChatRequest
import com.aiai.network.model.response.ApiResponse
import com.aiai.network.model.response.ChatResponse
import com.aiai.network.model.response.PagedResponse
import com.aiai.network.model.response.StreamResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import kotlinx.coroutines.flow.Flow

/**
 * 聊天相关API服务接口。
 *
 * 提供聊天消息发送、流式聊天、历史消息管理等接口。
 *
 * 接口列表：
 * - POST /chat/send - 发送消息
 * - POST /chat/stream - 流式聊天（SSE）
 * - GET /chat/history/{conversationId} - 获取历史消息
 * - DELETE /chat/message/{messageId} - 删除消息
 * - DELETE /chat/conversation/{conversationId}/clear - 清空对话
 */
interface ChatApiService {

    /**
     * 发送聊天消息。
     *
     * @param request 聊天请求体
     * @return 聊天响应
     */
    @POST("chat/send")
    suspend fun sendMessage(@Body request: ChatRequest): ApiResponse<ChatResponse>

    /**
     * 流式聊天（Server-Sent Events）。
     *
     * @param request 聊天请求体，stream=true
     * @return 流式响应Flow
     */
    @POST("chat/stream")
    suspend fun streamChat(@Body request: ChatRequest): Flow<StreamResponse>

    /**
     * 获取历史消息列表。
     *
     * @param conversationId 会话ID
     * @param page 页码，从1开始
     * @param pageSize 每页数量
     * @return 分页的历史消息
     */
    @GET("chat/history/{conversationId}")
    suspend fun getHistoryMessages(
        @Path("conversationId") conversationId: String,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20,
    ): ApiResponse<PagedResponse<ChatResponse>>

    /**
     * 删除单条消息。
     *
     * @param messageId 消息ID
     * @return 操作结果
     */
    @DELETE("chat/message/{messageId}")
    suspend fun deleteMessage(@Path("messageId") messageId: String): ApiResponse<Unit>

    /**
     * 清空指定会话的所有消息。
     *
     * @param conversationId 会话ID
     * @return 操作结果
     */
    @DELETE("chat/conversation/{conversationId}/clear")
    suspend fun clearConversation(@Path("conversationId") conversationId: String): ApiResponse<Unit>

    /**
     * 重新生成回答。
     *
     * @param conversationId 会话ID
     * @param messageId 用户消息ID
     * @return 新的聊天响应
     */
    @POST("chat/regenerate")
    suspend fun regenerateResponse(
        @Query("conversation_id") conversationId: String,
        @Query("message_id") messageId: String,
    ): ApiResponse<ChatResponse>

    /**
     * 编辑消息并重新发送。
     *
     * @param messageId 原消息ID
     * @param request 新的聊天请求
     * @return 新的聊天响应
     */
    @POST("chat/message/{messageId}/edit")
    suspend fun editAndResend(
        @Path("messageId") messageId: String,
        @Body request: ChatRequest,
    ): ApiResponse<ChatResponse>

    /**
     * 上传并发送带附件的消息。
     *
     * @param request 聊天请求体（包含附件信息）
     * @return 聊天响应
     */
    @POST("chat/send/with-attachment")
    suspend fun sendMessageWithAttachment(@Body request: ChatRequest): ApiResponse<ChatResponse>
}
