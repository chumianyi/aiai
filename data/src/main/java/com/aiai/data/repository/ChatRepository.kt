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
package com.aiai.data.repository

import com.aiai.data.entity.MessageEntity
import com.aiai.network.model.request.ChatRequest
import com.aiai.network.model.response.ChatResponse
import kotlinx.coroutines.flow.Flow

/**
 * 聊天仓库接口。
 *
 * 提供聊天消息发送、流式聊天、消息CRUD等功能。
 */
interface ChatRepository {

    /**
     * 发送聊天消息。
     *
     * @param request 聊天请求
     * @return 聊天响应
     */
    suspend fun sendMessage(request: ChatRequest): Result<ChatResponse>

    /**
     * 流式聊天。
     *
     * @param request 聊天请求
     * @return 流式响应Flow
     */
    fun streamChat(request: ChatRequest): Flow<String>

    /**
     * 获取会话消息列表。
     *
     * @param conversationId 会话ID
     * @return 消息列表Flow
     */
    fun getMessages(conversationId: String): Flow<List<MessageEntity>>

    /**
     * 分页获取消息。
     *
     * @param conversationId 会话ID
     * @param page 页码
     * @param pageSize 每页数量
     * @return 消息列表
     */
    suspend fun getMessagesPaged(conversationId: String, page: Int, pageSize: Int): List<MessageEntity>

    /**
     * 保存消息到本地数据库。
     *
     * @param message 消息实体
     */
    suspend fun saveMessage(message: MessageEntity)

    /**
     * 批量保存消息。
     *
     * @param messages 消息列表
     */
    suspend fun saveMessages(messages: List<MessageEntity>)

    /**
     * 删除消息。
     *
     * @param messageId 消息ID
     */
    suspend fun deleteMessage(messageId: String)

    /**
     * 清空会话消息。
     *
     * @param conversationId 会话ID
     */
    suspend fun clearMessages(conversationId: String)

    /**
     * 更新消息状态。
     *
     * @param messageId 消息ID
     * @param status 新状态
     */
    suspend fun updateMessageStatus(messageId: String, status: String)

    /**
     * 搜索消息。
     *
     * @param conversationId 会话ID
     * @param keyword 关键词
     * @return 匹配的消息列表
     */
    fun searchMessages(conversationId: String, keyword: String): Flow<List<MessageEntity>>

    /**
     * 同步消息到服务器。
     *
     * @param conversationId 会话ID
     * @return 同步结果
     */
    suspend fun syncMessages(conversationId: String): Result<Unit>
}
